package com.kartinimedia.albumcantik.activity

import android.content.Context
import android.os.Bundle
import android.text.InputType
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.kaopiz.kprogresshud.KProgressHUD
import com.kartinimedia.albumcantik.R
import com.kartinimedia.albumcantik.model.LoginModel
import com.kartinimedia.albumcantik.presenter.LoginPresenter
import com.kartinimedia.albumcantik.utils.Const
import com.kartinimedia.albumcantik.utils.kProgressHUD
import com.kartinimedia.albumcantik.view.LoginView
import kotlinx.android.synthetic.main.activity_login.*
import org.jetbrains.anko.*
import org.jetbrains.anko.sdk27.coroutines.onClick

class LoginActivity : AppCompatActivity(), LoginView {

    private lateinit var loading: KProgressHUD
    private val presenter = LoginPresenter()
    private lateinit var edtEmail: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        onAttachView()
    }

    override fun showLoading() {
        loading.show()
    }

    @Suppress("SENSELESS_COMPARISON")
    override fun suksesLogin(it: LoginModel) {
        loading.dismiss()
        // simpan data user di sharedpreferences
        getSharedPreferences(Const.userPref, Context.MODE_PRIVATE).edit().apply {
            putInt(Const.id, it.id)
            putString(Const.username, it.username)
            putString(Const.email, it.email)
            putString(Const.token, it.accessToken)
            putString(Const.fullName, it.profile.fullName)
            putInt(Const.genderId, it.profile.gender.id)
            putString(Const.namaGender, it.profile.gender.gender)
            putString(Const.noHp, it.profile.nomerHp.toString())
            if (it.profile.alamat != null) putInt(Const.alamat, it.profile.alamat.id)
            putString(Const.roles, it.roles)
            // status user login menjadi true
            putBoolean(Const.status, true)
            apply()
        }

        onBackPressed()
    }

    override fun error(msg: String) {
        alert {
            isCancelable = false
            message = msg
            okButton {
                it.dismiss()
            }
        }.show()
    }

    override fun hideLoading() {
        loading.dismiss()
    }

    override fun onAttachView() {
        presenter.onAttach(this)

        btn_daftar_akun.setOnClickListener {
            startActivity<SignupActivity>()
        }

        loading = kProgressHUD(this, getString(R.string.mohon_tunggu))

        btn_masuk.onClick {
            if (formValidation()) presenter.login(
                username = edt_username_login.text.toString(),
                password = edt_password_login.text.toString(),
                context = this@LoginActivity
            )
        }

        btn_kirim_email.onClick {
            alertCustom(getString(R.string.reset_password), 1)
        }

        btn_lupa_password.onClick {
            alertCustom(getString(R.string.resend_email_verifikasi), 2)
        }
    }

    override fun onDetachView() {
        presenter.onDetach()
        presenter.dispo()
    }

    override fun onDestroy() {
        super.onDestroy()
        onDetachView()
    }

    private fun alertCustom(text: String, code: Int) {
        alert {
            isCancelable = false
            title = text
            customView {
                verticalLayout {
                    padding = dip(16)
                    edtEmail = editText {
                        hint = getString(R.string.email)
                        inputType = InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
                    }
                    positiveButton(getString(R.string.submit)) {
                        if (edtEmail.text.isNotEmpty())
                            if (code == 1) presenter.resendEmail(edtEmail.text.toString(), this@LoginActivity)
                            else presenter.lupaPass(email =  edtEmail.text.toString(), context =  this@LoginActivity)
                    }
                    negativeButton(getString(R.string.batal)) {
                        it.dismiss()
                    }
                }
            }
        }.show()
    }

    override fun suksesResend() {
        alert {
            isCancelable = false
            message = getString(R.string.email_dikirim_kembali)
            okButton {
                it.dismiss()
            }
        }.show()
    }

    override fun suksesReqLupaPass() {
        alert {
            isCancelable = false
            message = getString(R.string.email_reset_dikirim)
            okButton {
                it.dismiss()
            }
        }.show()
    }

    private fun formValidation(): Boolean {

        var valid = true

        // cek form kosong
        if (edt_username_login.text.isEmpty()) {
            edt_username_login.error = getString(R.string.username_kosong)
            valid = false
        }
        if (edt_password_login.text.isEmpty()) {
            edt_password_login.error = getString(R.string.password_kosong)
            valid = false
        }

        return valid
    }
}
