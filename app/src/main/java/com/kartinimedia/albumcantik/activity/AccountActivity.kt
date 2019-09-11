package com.kartinimedia.albumcantik.activity

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Typeface
import android.os.Bundle
import android.text.InputType
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.appcompat.app.AppCompatActivity
import com.kaopiz.kprogresshud.KProgressHUD
import com.kartinimedia.albumcantik.R
import com.kartinimedia.albumcantik.model.UpdateProfileModel
import com.kartinimedia.albumcantik.presenter.AccountPresenter
import com.kartinimedia.albumcantik.utils.*
import com.kartinimedia.albumcantik.view.AccountView
import kotlinx.android.synthetic.main.activity_account.*
import org.jetbrains.anko.*
import org.jetbrains.anko.sdk27.coroutines.onClick

class AccountActivity : AppCompatActivity(), AccountView {

    private lateinit var userPref: SharedPreferences
    private var genderId = 2
    private lateinit var rbPria: RadioButton
    private lateinit var rbWanita: RadioButton
    private lateinit var loading: KProgressHUD
    private val presenter = AccountPresenter()
    private var edtUlangiPass: EditText? = null
    private var edtPassBaru: EditText? = null
    private var edtPassLama: EditText? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account)
        onAttachView()
    }

    private fun setDataAkun() {
        edt_username_akun.setText(userPref.getString(Const.username, ""))
        edt_email_akun.setText(userPref.getString(Const.email, ""))
        edt_nama_lengkap_akun.setText(userPref.getString(Const.fullName, ""))
        tv_role.text = getRoles(this).toUpperCase()
        tv_nilai_gender_akun.text = userPref.getString(Const.namaGender, "")
        genderId = userPref.getInt(Const.genderId, 2)
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
                            text = getGenderNama1(this@AccountActivity)
                            tag = getGenderId1(this@AccountActivity)
                        }
                        rbWanita = radioButton {
                            text = getGenderNama2(this@AccountActivity)
                            tag = getGenderId2(this@AccountActivity)
                        }.lparams {
                            leftMargin = dip(16)
                        }

                        when (genderId) {
                            getGenderId1(this@AccountActivity) -> check(rbPria.id)
                            getGenderId2(this@AccountActivity) -> check(rbWanita.id)
                        }
                    }
                }
            }
            positiveButton(getString(R.string.pilih)) {
                tv_nilai_gender_akun.text = if (rbPria.isChecked ) getGenderNama1(this@AccountActivity) else getGenderNama2(this@AccountActivity)
                genderId = if (rbPria.isChecked) rbPria.tag.toString().toInt() else rbWanita.tag.toString().toInt()
            }
            negativeButton(getString(R.string.batal)) {
                it.dismiss()
            }
        }.show()
    }

    override fun showLoading() {
        loading.show()
    }

    override fun hideLoading() {
        loading.dismiss()
    }

    override fun suksesUpdate(it: UpdateProfileModel?) {
        loading.dismiss()
        alert {
            isCancelable = false
            message = getString(R.string.update_berhasil)
            okButton {
                it.dismiss()
            }
        }.show()

        userPref.edit().apply {
            putString(Const.fullName, edt_nama_lengkap_akun.text.toString())
            putInt(Const.genderId, genderId)
            putString(Const.namaGender, it?.profile?.gender?.gender)
            apply()
        }

        setDataAkun()
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

    override fun onAttachView() {
        presenter.onAttach(this)

        setSupportActionBar(toolbar_account)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            title = getString(R.string.akunku)
        }

        loading = kProgressHUD(this, getString(R.string.mohon_tunggu))

        btn_keluar.onClick {
            alert {
                isCancelable = false
                message = getString(R.string.yakin_keluar)
                positiveButton(getString(R.string.ya)) {
                    getSharedPreferences(Const.userPref, Context.MODE_PRIVATE).edit().clear().apply()
                    onBackPressed()
                }
                negativeButton(getString(R.string.batal)) {
                    it.dismiss()
                }
            }.show()
        }

        btn_gender_akun.onClick {
            alertGender()
        }

        userPref = getSharedPreferences(Const.userPref, Context.MODE_PRIVATE)
        setDataAkun()

        btn_ganti_pass.onClick {
            alertGantiPass()
        }

        btn_alamat.onClick {
            startActivity<AlamatActivity>()
        }

        btn_riwayat_order.onClick {
            startActivity<ListOrderActivity>()
        }
    }

    private fun alertGantiPass() {
        alert {
            isCancelable = false
            title = getString(R.string.lupa_password)
            customView {
                verticalLayout {
                    padding = dip(16)
                    edtPassLama = autoCompleteTextView {
                        hint = getString(R.string.pass_lama)
                        inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                        typeface = Typeface.DEFAULT
                    }
                    edtPassBaru = autoCompleteTextView {
                        hintResource = R.string.password_baru
                        inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                        typeface = Typeface.DEFAULT
                    }
                    edtUlangiPass = autoCompleteTextView {
                        hintResource = R.string.ulangi_password
                        inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                        typeface = Typeface.DEFAULT
                    }
                }
            }
            positiveButton(getString(R.string.submit)) {
                if (edtPassLama?.text?.isNotEmpty() as Boolean
                    || edtPassBaru?.text?.isNotEmpty() as Boolean
                    || edtUlangiPass?.text?.isNotEmpty() as Boolean) presenter.updateProfile(

                    context = this@AccountActivity,
                    fullName = edt_nama_lengkap_akun.text.toString(),
                    genderId = genderId,
                    passBaru = edtPassBaru?.text.toString(),
                    passLama = edtPassLama?.text.toString(),
                    passRepeat = edtUlangiPass?.text.toString(),
                    alamatId = getAlamatUtama(this@AccountActivity)
                )

            }
            negativeButton(getString(R.string.batal)) {
                it.dismiss()
            }
        }.show()
    }


    override fun onDetachView() {
        presenter.onDetach()
        presenter.dispo()
    }

    override fun onDestroy() {
        super.onDestroy()
        onDetachView()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_account, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        when (item?.itemId) {
            R.id.simpan -> {
                presenter.updateProfile(
                    context = this,
                    fullName = edt_nama_lengkap_akun.text.toString(),
                    genderId = genderId,
                    passBaru = null,
                    passLama = null,
                    passRepeat = null,
                    alamatId = getAlamatUtama(this@AccountActivity)
                )
            }
            else -> onBackPressed()
        }

        return true
    }
}
