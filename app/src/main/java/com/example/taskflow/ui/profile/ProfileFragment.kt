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
import com.example.taskflow.data.repository.UserRepository
import com.example.taskflow.databinding.FragmentProfileBinding
import com.example.taskflow.session.SessionManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import com.example.taskflow.data.local.entity.UserEntity
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class ProfileFragment : Fragment(
    R.layout.fragment_profile
) {

    private var _binding:
            FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private lateinit var sessionManager:
            SessionManager
    private var currentUser: UserEntity? = null

    private val userRepository:
            UserRepository by lazy {
        UserRepository(
            DatabaseProvider
                .getDatabase(
                    requireContext()
                )
                .userDao()
        )
    }

    private val pickMedia =
        registerForActivityResult(
            ActivityResultContracts.PickVisualMedia()
        ) { uri ->
            if (uri != null) {
                // Hiển thị ngay lên giao diện cho mượt
                binding.ivAvatar.setImageURI(
                    uri
                )

                // Copy ảnh và lưu vào DB ở Background Thread
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

    // --- HÀM HỖ TRỢ COPY ẢNH VÀO THƯ MỤC CỦA APP ---
    private fun saveImageToInternalStorage(
        uri: Uri
    ): String {
        val inputStream =
            requireContext()
                .contentResolver
                .openInputStream(uri)

        val file = File(
            requireContext().filesDir,
            "avatar_${sessionManager.getCurrentUserId()}.jpg"
        )

        val outputStream = FileOutputStream(file)

        inputStream?.copyTo(outputStream)
        inputStream?.close()
        outputStream.close()

        return file.absolutePath
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(
            view,
            savedInstanceState
        )

        _binding =
            FragmentProfileBinding.bind(
                view
            )

        sessionManager =
            SessionManager(
                requireContext()
            )

        val userId =
            sessionManager
                .getCurrentUserId()

        CoroutineScope(
            Dispatchers.Main
        ).launch {

            val user =
                userRepository
                    .getUserById(
                        userId
                    )

            user?.let {

                //thay doi
                currentUser = it
                //
                binding.tvUsername.text =
                    it.username

                // --- HIỂN THỊ ẢNH TỪ DATABASE KHI LOAD TRANG ---
                it.avatarPath?.let { path ->
                    val file = File(path)
                    if (file.exists()) {
                        binding.ivAvatar.setImageURI(Uri.fromFile(file))
                    }
                }
            }
        }

        binding.ivAvatar
            .setOnClickListener {
                pickMedia.launch(
                    PickVisualMediaRequest(
                        ActivityResultContracts.PickVisualMedia.ImageOnly
                    )
                )
            }

        binding.btnEditProfile
            .setOnClickListener {
                Toast.makeText(requireContext(), "Chỉnh sửa hồ sơ", Toast.LENGTH_SHORT).show()
            }

        binding.menuAccount
            .setOnClickListener {
                Toast.makeText(requireContext(), "Thông tin tài khoản", Toast.LENGTH_SHORT).show()
            }

        binding.menuNotification
            .setOnClickListener {
                Toast.makeText(requireContext(), "Cài đặt thông báo", Toast.LENGTH_SHORT).show()
            }

        binding.menuPassword
            .setOnClickListener {
                currentUser?.let { user ->
                    showChangePasswordDialog(user)
                } ?: run {
                    Toast.makeText(requireContext(), "Đang tải dữ liệu...", Toast.LENGTH_SHORT).show()
                }
            }

        binding.btnLogout
            .setOnClickListener {

                sessionManager.logout()

                findNavController()
                    .navigate(
                        R.id.action_profileFragment_to_loginFragment
                    )
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    // --- HÀM HỖ TRỢ: BẬT HỘP THOẠI ĐỔI MẬT KHẨU (ĐÃ CẬP NHẬT) ---
    private fun showChangePasswordDialog(user: UserEntity) {
        // Nạp giao diện dialog_change_password.xml
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_change_password, null)

        // Ánh xạ thêm ô nhập Email
        val edtEmail = dialogView.findViewById<EditText>(R.id.edtEmail)
        val edtOldPassword = dialogView.findViewById<EditText>(R.id.edtOldPassword)
        val edtNewPassword = dialogView.findViewById<EditText>(R.id.edtNewPassword)

        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Đổi mật khẩu")
            .setView(dialogView)
            .setPositiveButton("Lưu thay đổi") { dialog, _ ->
                // Lấy dữ liệu người dùng nhập vào
                val inputEmail = edtEmail.text.toString().trim()
                val oldPass = edtOldPassword.text.toString()
                val newPass = edtNewPassword.text.toString()

                // 1. Kiểm tra Email nhập vào có khớp với Email của tài khoản không
                if (inputEmail != user.email) {
                    Toast.makeText(requireContext(), "Email xác nhận không chính xác!", Toast.LENGTH_SHORT).show()
                    return@setPositiveButton
                }

                // 2. Kiểm tra mật khẩu cũ có khớp với Database không
                if (oldPass != user.password) {
                    Toast.makeText(requireContext(), "Mật khẩu hiện tại không đúng!", Toast.LENGTH_SHORT).show()
                    return@setPositiveButton
                }

                // 3. Kiểm tra điều kiện mật khẩu mới
                if (newPass.isEmpty() || newPass.length < 6) {
                    Toast.makeText(requireContext(), "Mật khẩu mới phải có ít nhất 6 ký tự!", Toast.LENGTH_SHORT).show()
                    return@setPositiveButton
                }

                // 4. Thực hiện lưu vào Database
                CoroutineScope(Dispatchers.IO).launch {
                    userRepository.updatePassword(user.id, newPass)

                    withContext(Dispatchers.Main) {
                        Toast.makeText(requireContext(), "Đã đổi mật khẩu thành công!", Toast.LENGTH_SHORT).show()
                        // Cập nhật lại biến currentUser để lần sau đổi tiếp không bị lỗi
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