package com.kartinimedia.albumcantik.activity

import android.os.Bundle
import android.util.Patterns
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.appcompat.app.AppCompatActivity
import com.kaopiz.kprogresshud.KProgressHUD
import com.kartinimedia.albumcantik.R
import com.kartinimedia.albumcantik.presenter.SignupPresenter
import com.kartinimedia.albumcantik.utils.*
import com.kartinimedia.albumcantik.view.SignupView
import kotlinx.android.synthetic.main.activity_signup.*
import org.jetbrains.anko.*
import org.jetbrains.anko.sdk27.coroutines.onClick

class SignupActivity : AppCompatActivity(), SignupView {

    private var presenter = SignupPresenter()
    private var genderId = 2
    private lateinit var rbPria: RadioButton
    private lateinit var rbWanita: RadioButton
    private lateinit var loading: KProgressHUD

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        onAttachView()
    }

    override fun onAttachView() {
        presenter.onAttach(this)

        // toolbar
        setSupportActionBar(toolbar_signup)
        supportActionBar?.apply {
            title = ""
            setDisplayHomeAsUpEnabled(true)
        }

        btn_daftar.onClick {
            if (formValidation())
                presenter.signup(
                    username = edt_username.text.toString(),
                    email = edt_email.text.toString(),
                    password = edt_password.text.toString(),
                    fullName = edt_nama_lengkap.text.toString(),
                    genderId = genderId,
                    context = this@SignupActivity
                )
        }

        btn_gender.onClick {
            alertGender()
        }

        testing.onClick {
            edt_username.setText("hillme30")
            edt_email.setText("gg@gmail.com")
            edt_password.setText("1234567890")
            edt_ulang_password.setText("1234567890")
            edt_nama_lengkap.setText("mantul")
        }

        loading = kProgressHUD(this, getString(R.string.mohon_tunggu))
    }

    override fun onDetachView() {
        presenter.onDetach()
        presenter.dispo()
    }

    override fun onDestroy() {
        super.onDestroy()
        onDetachView()
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

    override fun suksesRegister() {
        loading.dismiss()

        alert {
            isCancelable = false
            title = getString(R.string.register_berhasil)
            message = getString(R.string.cek_email)
            okButton {
                onBackPressed()
            }
        }.show()
    }

    override fun showLoading() {
        loading.show()
    }

    override fun hideLoading() {
        loading.dismiss()
    }

    private fun alertGender() {
        alert {
            isCancelable = false
            customView {
                verticalLayout {
                    padding = dip(16)
                    radioGroup {
                        orientation = RadioGroup.HORIZONTAL
                        rbPria = radioButton {
                            text = getGenderNama1(this@SignupActivity)
                            tag = getGenderId1(this@SignupActivity)
                        }
                        rbWanita = radioButton {
                            text = getGenderNama2(this@SignupActivity)
                            tag = getGenderId2(this@SignupActivity)
                        }.lparams {
                            leftMargin = dip(16)
                        }

                        when (genderId) {
                            getGenderId1(this@SignupActivity) -> check(rbPria.id)
                            getGenderId2(this@SignupActivity) -> check(rbWanita.id)
                        }
                    }
                }
            }
            positiveButton(getString(R.string.pilih)) {
                btn_nilai_gender.text = if (rbPria.isChecked ) getGenderNama1(this@SignupActivity) else getGenderNama2(this@SignupActivity)
                genderId = if (rbPria.isChecked) rbPria.tag.toString().toInt() else rbWanita.tag.toString().toInt()
            }
            negativeButton(getString(R.string.batal)) {
                it.dismiss()
            }
        }.show()
    }

    private fun formValidation(): Boolean {

        var valid = true

        // cek form kosong
        if (edt_username.text.isEmpty()) {
            edt_username.error = getString(R.string.username_kosong)
            valid = false
        }
        if (edt_email.text.isEmpty()) {
            edt_email.error = getString(R.string.email_kosong)
            valid = false
        }
        if (edt_password.text.isEmpty()) {
            edt_password.error = getString(R.string.password_kosong)
            valid = false
        }
        if (edt_nama_lengkap.text.isEmpty()) {
            edt_nama_lengkap.error = getString(R.string.nama_kosong)
            valid = false
        }

        // cek format username
        val regex = "^[\\p{L} .'-]+$"
        if (edt_username.text.contains(" ")) {
            edt_username.error = getString(R.string.username_ada_spasi)
            valid = false
        }
        if (edt_username.text.contains(regex)) {
            edt_username.error = getString(R.string.username_tidak_benar)
            valid = false
        }

        // cek format email
        if (!Patterns.EMAIL_ADDRESS.matcher(edt_email.text.toString()).matches()) {
            edt_email.error = getString(R.string.email_tidak_benar)
            valid = false
        }

        // cek format password
        if (edt_password.text.length < 6) {
            edt_password.error = getString(R.string.password_6_karakter)
            valid = false
        }
        if (edt_password.text.toString() != edt_ulang_password.text.toString()) {
            edt_ulang_password.error = getString(R.string.password_beda)
            valid = false
        }

        return valid
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
