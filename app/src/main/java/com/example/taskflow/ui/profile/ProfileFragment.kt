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
import com.example.taskflow.databinding.FragmentProfileBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.example.taskflow.data.firebase.FirebaseAuthRepository
import com.example.taskflow.data.firebase.FirebaseUserRepository
import com.example.taskflow.data.firebase.model.FirebaseUser
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import androidx.fragment.app.activityViewModels
import com.example.taskflow.viewmodel.UserViewModel

class ProfileFragment : Fragment(
    R.layout.fragment_profile
) {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private var currentUser: FirebaseUser? = null

    private val authRepository =
        FirebaseAuthRepository()

    private val userRepository =
        FirebaseUserRepository()

    private val userViewModel: UserViewModel by activityViewModels()

    private val pickMedia =
        registerForActivityResult(
            ActivityResultContracts.PickVisualMedia()
        ) { uri ->

            if (uri != null) {

                binding.ivAvatar.setImageURI(uri)

                Toast.makeText(
                    requireContext(),
                    "Tạm thời chỉ hiển thị avatar. Sẽ lưu Firebase Storage ở bước sau.",
                    Toast.LENGTH_SHORT
                ).show()

            } else {

                Toast.makeText(
                    requireContext(),
                    "Chưa chọn ảnh",
                    Toast.LENGTH_SHORT
                ).show()

            }

        }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentProfileBinding.bind(view)


        // 1. Lấy dữ liệu người dùng khi load trang
        userViewModel.loadCurrentUser()

        userViewModel.currentUser.observe(viewLifecycleOwner) { user ->

            if (user == null) return@observe

            currentUser = user

            binding.tvUsername.text =
                user.username

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
            userViewModel.clearUser()

            authRepository.logout()

            findNavController().navigate(
                R.id.action_profileFragment_to_loginFragment
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    // 1. Hộp thoại Chỉnh sửa hồ sơ
    private fun showEditProfileDialog(user: FirebaseUser) {
        val dialogView =
            LayoutInflater.from(requireContext()).inflate(R.layout.dialog_edit_profile, null)
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

                if (newUsername.isEmpty()) {
                    Toast.makeText(
                        requireContext(),
                        "Tên người dùng không được để trống!",
                        Toast.LENGTH_SHORT
                    ).show()
                    return@setPositiveButton
                }

                userRepository.updateProfile(

                    username = newUsername,

                    phone = newPhone,

                    avatarUrl = user.avatarUrl

                ) { success ->

                    if (success) {

                        currentUser = currentUser?.copy(

                            username = newUsername,

                            phone = newPhone

                        )

                        userViewModel.refreshUser()

                        Toast.makeText(
                            requireContext(),
                            "Đã cập nhật hồ sơ",
                            Toast.LENGTH_SHORT
                        ).show()

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
    private fun showAccountInfoDialog(user: FirebaseUser) {
        val dialogView =
            LayoutInflater.from(requireContext()).inflate(R.layout.dialog_account_info, null)
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
    private fun showChangePasswordDialog(user: FirebaseUser) {
        val dialogView =
            LayoutInflater.from(requireContext()).inflate(R.layout.dialog_change_password, null)
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
                    Toast.makeText(
                        requireContext(),
                        "Email xác nhận không chính xác!",
                        Toast.LENGTH_SHORT
                    ).show()
                    return@setPositiveButton
                }



                if (newPass.isEmpty() || newPass.length < 6) {
                    Toast.makeText(
                        requireContext(),
                        "Mật khẩu mới phải có ít nhất 6 ký tự!",
                        Toast.LENGTH_SHORT
                    ).show()
                    return@setPositiveButton
                }

                val firebaseUser =
                    FirebaseAuth
                        .getInstance()
                        .currentUser

                if (firebaseUser != null) {

                    val credential =

                        EmailAuthProvider.getCredential(

                            user.email,

                            oldPass

                        )

                    firebaseUser

                        .reauthenticate(credential)

                        .addOnSuccessListener {

                            firebaseUser

                                .updatePassword(newPass)

                                .addOnSuccessListener {

                                    Toast.makeText(

                                        requireContext(),

                                        "Đổi mật khẩu thành công",

                                        Toast.LENGTH_SHORT

                                    ).show()

                                    dialog.dismiss()

                                }

                                .addOnFailureListener {

                                    Toast.makeText(

                                        requireContext(),

                                        it.localizedMessage,

                                        Toast.LENGTH_LONG

                                    ).show()

                                }

                        }

                        .addOnFailureListener {

                            Toast.makeText(

                                requireContext(),

                                "Mật khẩu hiện tại không đúng",

                                Toast.LENGTH_SHORT

                            ).show()

                        }

                }


            }
            .setNegativeButton("Hủy") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }
}
