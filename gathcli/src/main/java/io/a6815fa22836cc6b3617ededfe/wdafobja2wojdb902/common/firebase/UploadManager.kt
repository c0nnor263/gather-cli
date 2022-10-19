package io.a6815fa22836cc6b3617ededfe.wdafobja2wojdb902.common.firebase

import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import io.a6815fa22836cc6b3617ededfe.wdafobja2wojdb902.data.model.UserDataModel
import io.a6815fa22836cc6b3617ededfe.wdafobja2wojdb902.utils.enums.UploadDataType
import io.a6815fa22836cc6b3617ededfe.wdafobja2wojdb902.utils.extension.databaseEmptyOrNot
import io.github.a26197993b77e31a4.o7b471d74a5346efb54aa326b892daf01d914ce99.ObfustringThis

@ObfustringThis
internal class UploadManager(databaseUrl: String, countryCode: String) {
	private val database = Firebase.databaseEmptyOrNot(databaseUrl)
	private val ref = database.reference.child(countryCode)
	private val newKey = ref.push().key.toString()


	suspend fun uploadData(
		cacheNodeKey: String?,
		uploadDataType: UploadDataType,
		data: Any,
		saveNewParent: suspend (String) -> Unit
	) {
		val parentKey = cacheNodeKey ?: newKey.also {
			saveNewParent(newKey)
			Log.i("GathClient", "saveNodeParentKey: $it")
		}

		val query = ref.equalTo(null, parentKey)
		Log.i("TAG", "uploadData parent: $parentKey")
		query.keepSynced(true)
		query.addListenerForSingleValueEvent(object : ValueEventListener {
			override fun onDataChange(snapshot: DataSnapshot) {

				val operation: (String) -> Unit = { key ->
					Log.i(
						"TAG",
						"onDataChange operation: \nkey:$key  \nfield:${uploadDataType.field}"
					)
					ref.child(key).child(uploadDataType.field).setValue(data).addOnFailureListener {
						Crashlog.record(it)
					}
					if (data is UserDataModel) checkDuplicate(data)
				}

				if (snapshot.exists()) {
					// EXIST
					snapshot.children.first().key?.let { operation(it) }
					Log.i("GathClient", "uploadToFirebase:\nEXIST \n$data")
				} else {
					// NOT EXIST
					operation(parentKey)
					Log.i("GathClient", "uploadToFirebase:\nNOT EXIST \n$data")
				}
			}

			override fun onCancelled(error: DatabaseError) {}
		})
	}


	private fun checkDuplicate(dataForCheck: UserDataModel) {
		if (
			dataForCheck.deposit.not() &&
			dataForCheck.phone.isBlank() &&
			dataForCheck.email.isBlank()
		) return
		val query = ref.orderByKey()
		query.addListenerForSingleValueEvent(object : ValueEventListener {
			override fun onDataChange(snapshot: DataSnapshot) {
				try {
					if (snapshot.exists()) {
						snapshot.children.reversed().forEachIndexed { index, dataSnapshot ->
							if (index == 0) return@forEachIndexed
							val value = dataSnapshot.getValue<Map<String, Any?>>()
								?.get(UploadDataType.EMAIL.field).toString()
							val userData = com.google.gson.Gson().fromJson(
								value,
								UserDataModel::class.java
							)
							userData?.let { node ->
								if (node.email == dataForCheck.email &&
									node.phone == dataForCheck.phone &&
									node.deposit == dataForCheck.deposit
								) {
									dataSnapshot.key?.let { ref.child(it).removeValue() }
									Log.i("GathClient", "checkForDuplicate: \nDUPLICATE \n$node")
								}
							}
						}
					}
				} catch (e: Exception) {
					Crashlog.record(e)
				}
			}

			override fun onCancelled(error: DatabaseError) {}
		})
	}


}