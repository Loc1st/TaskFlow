package com.example.taskflow.ui.profile

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.taskflow.R
import com.example.taskflow.data.local.db.DatabaseProvider
import com.example.taskflow.data.local.entity.UserEntity
import com.example.taskflow.data.repository.UserRepository
import com.example.taskflow.databinding.FragmentProfileBinding
import com.example.taskflow.session.SessionManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream

class ProfileFragment : Fragment(
    R.layout.fragment_profile
) {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private lateinit var sessionManager: SessionManager
    private var currentUser: UserEntity? = null

    private val userRepository: UserRepository by lazy {
        UserRepository(
            DatabaseProvider.getDatabase(requireContext()).userDao()
        )
    }

    private val pickMedia =
        registerForActivityResult(
            ActivityResultContracts.PickVisualMedia()
        ) { uri ->
            if (uri != null) {
                binding.ivAvatar.setImageURI(uri)

                CoroutineScope(Dispatchers.IO).launch {
                    val savedPath = saveImageToInternalStorage(uri)

                    val userId = sessionManager.getCurrentUserId()
                    userRepository.updateAvatar(userId, savedPath)

                    withContext(Dispatchers.Main) {
                        Toast.makeText(requireContext(), "Đã lưu ảnh thành công!", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(requireContext(), "Chưa chọn ảnh nào", Toast.LENGTH_SHORT).show()
            }
        }

    private fun saveImageToInternalStorage(uri: Uri): String {
        val inputStream = requireContext().contentResolver.openInputStream(uri)
        val file = File(requireContext().filesDir, "avatar_${sessionManager.getCurrentUserId()}.jpg")
        val outputStream = FileOutputStream(file)

        inputStream?.copyTo(outputStream)
        inputStream?.close()
        outputStream.close()

        return file.absolutePath
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentProfileBinding.bind(view)
        sessionManager = SessionManager(requireContext())
        val userId = sessionManager.getCurrentUserId()

        // 1. Lấy dữ liệu người dùng khi load trang
        CoroutineScope(Dispatchers.Main).launch {
            val user = userRepository.getUserById(userId)
            user?.let {
                currentUser = it
                binding.tvUsername.text = it.username

                it.avatarPath?.let { path ->
                    val file = File(path)
                    if (file.exists()) {
                        binding.ivAvatar.setImageURI(Uri.fromFile(file))
                    }
                }
            }
        }

        // 2. Các sự kiện Click trên giao diện
        binding.ivAvatar.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        // Nút Chỉnh sửa hồ sơ
        binding.btnEditProfile.setOnClickListener {
            currentUser?.let { user ->
                showEditProfileDialog(user)
            } ?: Toast.makeText(requireContext(), "Đang tải dữ liệu...", Toast.LENGTH_SHORT).show()
        }

        // Nút Xem thông tin tài khoản
        binding.menuAccount.setOnClickListener {
            currentUser?.let { user ->
                showAccountInfoDialog(user)
            } ?: Toast.makeText(requireContext(), "Đang tải dữ liệu...", Toast.LENGTH_SHORT).show()
        }

        // Nút Đổi mật khẩu
        binding.menuPassword.setOnClickListener {
            currentUser?.let { user ->
                showChangePasswordDialog(user)
            } ?: Toast.makeText(requireContext(), "Đang tải dữ liệu...", Toast.LENGTH_SHORT).show()
        }

        // Nút Cài đặt thông báo (hiện tại để Toast)
        binding.menuNotification.setOnClickListener {
            Toast.makeText(requireContext(), "Cài đặt thông báo", Toast.LENGTH_SHORT).show()
        }

        // Nút Đăng xuất
        binding.btnLogout.setOnClickListener {
            sessionManager.logout()
            findNavController().navigate(R.id.action_profileFragment_to_loginFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }



    // 1. Hộp thoại Chỉnh sửa hồ sơ
    private fun showEditProfileDialog(user: UserEntity) {
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_edit_profile, null)
        val edtUsername = dialogView.findViewById<EditText>(R.id.edtEditUsername)
        val edtPhone = dialogView.findViewById<EditText>(R.id.edtEditPhone)
        val edtEmail = dialogView.findViewById<EditText>(R.id.edtEditEmail)

        edtUsername.setText(user.username)
        edtPhone.setText(user.phone ?: "")
        edtEmail.setText(user.email)

        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Chỉnh sửa hồ sơ")
            .setView(dialogView)
            .setPositiveButton("Lưu") { dialog, _ ->
                val newUsername = edtUsername.text.toString().trim()
                val newPhone = edtPhone.text.toString().trim()
                val newEmail = edtEmail.text.toString().trim()

                if (newUsername.isEmpty() || newEmail.isEmpty()) {
                    Toast.makeText(requireContext(), "Tên và Email không được để trống!", Toast.LENGTH_SHORT).show()
                    return@setPositiveButton
                }

                CoroutineScope(Dispatchers.IO).launch {
                    userRepository.updateProfile(user.id, newUsername, newEmail, newPhone)

                    withContext(Dispatchers.Main) {
                        Toast.makeText(requireContext(), "Đã cập nhật hồ sơ!", Toast.LENGTH_SHORT).show()
                        binding.tvUsername.text = newUsername
                        currentUser = currentUser?.copy(username = newUsername, email = newEmail, phone = newPhone)
                        dialog.dismiss()
                    }
                }
            }
            .setNegativeButton("Hủy") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    // 2. Hộp thoại Xem thông tin tài khoản
    private fun showAccountInfoDialog(user: UserEntity) {
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_account_info, null)
        val edtUsername = dialogView.findViewById<EditText>(R.id.edtInfoUsername)
        val edtPhone = dialogView.findViewById<EditText>(R.id.edtInfoPhone)
        val edtEmail = dialogView.findViewById<EditText>(R.id.edtInfoEmail)

        edtUsername.setText(user.username)
        edtPhone.setText(user.phone ?: "Chưa cập nhật")
        edtEmail.setText(user.email)

        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Thông tin tài khoản")
            .setView(dialogView)
            .setPositiveButton("Đóng") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    // 3. Hộp thoại Đổi mật khẩu
    private fun showChangePasswordDialog(user: UserEntity) {
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_change_password, null)
        val edtEmail = dialogView.findViewById<EditText>(R.id.edtEmail)
        val edtOldPassword = dialogView.findViewById<EditText>(R.id.edtOldPassword)
        val edtNewPassword = dialogView.findViewById<EditText>(R.id.edtNewPassword)

        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Đổi mật khẩu")
            .setView(dialogView)
            .setPositiveButton("Lưu thay đổi") { dialog, _ ->
                val inputEmail = edtEmail.text.toString().trim()
                val oldPass = edtOldPassword.text.toString()
                val newPass = edtNewPassword.text.toString()

                if (inputEmail != user.email) {
                    Toast.makeText(requireContext(), "Email xác nhận không chính xác!", Toast.LENGTH_SHORT).show()
                    return@setPositiveButton
                }

                if (oldPass != user.password) {
                    Toast.makeText(requireContext(), "Mật khẩu hiện tại không đúng!", Toast.LENGTH_SHORT).show()
                    return@setPositiveButton
                }

                if (newPass.isEmpty() || newPass.length < 6) {
                    Toast.makeText(requireContext(), "Mật khẩu mới phải có ít nhất 6 ký tự!", Toast.LENGTH_SHORT).show()
                    return@setPositiveButton
                }

                CoroutineScope(Dispatchers.IO).launch {
                    userRepository.updatePassword(user.id, newPass)

                    withContext(Dispatchers.Main) {
                        Toast.makeText(requireContext(), "Đã đổi mật khẩu thành công!", Toast.LENGTH_SHORT).show()
                        currentUser = currentUser?.copy(password = newPass)
                        dialog.dismiss()
                    }
                }
            }
            .setNegativeButton("Hủy") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }
}