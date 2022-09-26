package io.c0nnor263.gathcli.common.firebase

import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import io.c0nnor263.gathcli.utils.Consts
import io.c0nnor263.gathcli.utils.databaseEmptyOrNot

internal class UploadManager(databaseUrl: String, countryCode: String) {
    private val database = Firebase.databaseEmptyOrNot(databaseUrl)
    private val ref = database.reference.child(countryCode)
    private val key = ref.push().key.toString()

    fun uploadToFirebase(
        parentKey: String?,
        email: String,
        phoneNumber: String,
        deposit: Boolean,
        saveNewParent: (String) -> Unit
    ) {
        val newKey = parentKey ?: key.also {
            saveNewParent(it)
            Log.i("GathClient", "uploadData: saveParent $it")
        }
        val value = hashMapOf(
            Consts.email to email, Consts.phone to phoneNumber, Consts.deposit to deposit
        )

        processQuery(newKey, value)
    }

    private fun processQuery(
        queryKey: String, value: HashMap<String, *>
    ) {
        val query = ref.equalTo(
            null, queryKey
        )

        query.keepSynced(true)
        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    // EXIST
                    snapshot.children.forEach { dataSnapshot ->
                        dataSnapshot.key?.let {
                            ref.child(it).setValue(value)
                        }
                    }
                    Log.i("GathClient", "uploadToFirebase:\nEXIST \n$value")
                } else {
                    // NOT EXIST
                    ref.child(queryKey).setValue(value)
                    Log.i("GathClient", "uploadToFirebase:\nNOT EXIST \n$value")
                }
                if (value[Consts.deposit] == true && value[Consts.phone]?.toString()
                        ?.isNotBlank() == true && value[Consts.email]?.toString()
                        ?.isNotBlank() == true
                ) checkForDuplicate(value)
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

    private fun checkForDuplicate(valueForCheck: HashMap<String, *>) {
        val query = ref.orderByKey()
        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                if (snapshot.exists()) {
                    snapshot.children.reversed().forEachIndexed { index, dataSnapshot ->
                        if (index == 0) return@forEachIndexed

                        dataSnapshot.getValue<Map<String, Any>>()?.let { node ->

                            if (node[Consts.email] == valueForCheck[Consts.email] && node[Consts.phone] == valueForCheck[Consts.phone] && node[Consts.deposit] == valueForCheck[Consts.deposit]) {
                                dataSnapshot.key?.let { ref.child(it).removeValue() }
                                Log.i("GathClient", "checkForDuplicate: \nDUPLICATE \n$node")
                            }
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }
}