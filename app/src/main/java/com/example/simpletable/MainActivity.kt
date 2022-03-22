package com.example.simpletable

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.simpletable.databinding.ActivityMainBinding
import java.lang.Exception

class MainActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private val dbHelper: DBHelper by lazy {
        DBHelper.getInstance(applicationContext)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        insertDb()
        deleteDb()
        getAllDb()
    }

    override fun onDestroy() {
        dbHelper.close()
        super.onDestroy()
    }

    private fun showTxt(text: String) {

        binding.tvData.setText("")
        binding.tvData.append(text + "\n")
    }

    private fun clearEditTexts() {
        with(binding) {
            etFirstName.setText("")
            etLastName.setText("")
            etRewards.setText("")
        }
    }

    private fun insertDb() {
        clearEditTexts()
        binding.tvData.text = ""
        binding.btnAddMember.setOnClickListener {
            try{

                dbHelper.insertData(
                    binding.etFirstName.text.toString().trim(),
                    binding.etLastName.text.toString().trim(),
                    binding.etRewards.text.toString().trim()
                )
                clearEditTexts()
                showTxt("Data inserted")
            } catch (e:Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun deleteDb() {
        clearEditTexts()
        binding.tvData.text = ""
        binding.btnDelete.setOnClickListener {
            try{
                dbHelper.deleteData(binding.etUID.text.toString().trim())
                clearEditTexts()
                showTxt("Data Deleted")
            } catch (e:Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun getAllDb() {

        clearEditTexts()
        binding.tvData.text = ""

        binding.btnDisplayData.setOnClickListener {
            try {
                val selectResult : String = dbHelper.getAllData()
                showTxt(selectResult)
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
        }
    }
}