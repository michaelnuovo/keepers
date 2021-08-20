package com.example.keepers

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class MainActivity : AppCompatActivity() {

    lateinit var viewModel: MainViewModel
    lateinit var emailField: EditText
    lateinit var passWordField: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViews()
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        subscribeObservers()
        findViewById<Button>(R.id.btnLogin).setOnClickListener {
            if(!emailField.isEmailValid()) toast(getString(R.string.invalid_email))
            else if(!passWordField.isPasswordValid()) toast(getString(R.string.invalid_password))
            else toast(getString(R.string.logging_in))
        }
    }

    private fun findViews()
    {
        emailField = findViewById(R.id.etEnterEmailAddress)
        passWordField = findViewById(R.id.etEnterMasterPassword)
    }

    private fun subscribeObservers(){
        subscribeEmailField()
        subscribePassWordField()
    }

    private fun subscribeEmailField() = viewModel.email.observe(this, { email ->
        emailField.setText(email)
    })

    private fun subscribePassWordField() = viewModel.passWord.observe(this, { passWord ->
        passWordField.setText(passWord)
    })
}

fun Context.toast(msg: String, duration: Int = Toast.LENGTH_LONG) {
    Toast.makeText(this, msg, duration).show()
}

fun EditText.isEmailValid() : Boolean {
    return (!TextUtils.isEmpty(this.text)
            && Patterns.EMAIL_ADDRESS.matcher(this.text).matches())
}

fun EditText.isPasswordValid() = this.length() >= 6

class MainViewModel : ViewModel(){
    private val _passWord: MutableLiveData<String> = MutableLiveData()
    val passWord : LiveData<String>
        get() = _passWord

    private val _email: MutableLiveData<String> = MutableLiveData()
    val email : LiveData<String>
        get() = _email
}