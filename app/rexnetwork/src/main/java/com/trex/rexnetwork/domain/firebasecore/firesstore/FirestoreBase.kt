package com.trex.rexnetwork.domain.firebasecore.firesstore

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.trex.rexnetwork.data.BaseFirestoreResponse

enum class FireStoreExeptions {
    DOC_NOT_FOUND,
}

abstract class FirestoreBase<T : BaseFirestoreResponse>(
    private val collectionPath: String,
) {
    protected val db = FirebaseFirestore.getInstance()

    open fun getDocument(
        docId: String,
        clazz: Class<T>,
        onSuccess: (T) -> Unit,
        onFailure: (Exception) -> Unit,
    ) {
        val docRef = db.collection(collectionPath).document(docId)
        docRef
            .get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    val data = documentSnapshot.toObject(clazz) // Use the passed class parameter
                    if (data != null) {
                        onSuccess(data)
                    } else {
                        onFailure(Exception("Document does not match expected type."))
                    }
                } else {
                    onFailure(Exception(FireStoreExeptions.DOC_NOT_FOUND.toString()))
                }
            }.addOnFailureListener { exception ->
                onFailure(exception)
            }
    }

    open fun getAllDocuments(
        clazz: Class<T>,
        onSuccess: (List<T>) -> Unit,
        onFailure: (Exception) -> Unit,
    ) {
        db
            .collection(collectionPath)
            .get()
            .addOnSuccessListener { querySnapshot ->
                val documentList = mutableListOf<T>()
                for (document in querySnapshot.documents) {
                    val data = document.toObject(clazz)
                    if (data != null) {
                        documentList.add(data)
                    }
                }
                if (documentList.isNotEmpty()) {
                    onSuccess(documentList)
                } else {
                    onFailure(Exception("No documents found or documents do not match expected type."))
                }
            }.addOnFailureListener { exception ->
                onFailure(exception)
            }
    }

    open fun addOrUpdateDocument(
        docId: String? = null,
        data: T,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit,
    ) {
        val docRef =
            if (docId == null) {
                db.collection(collectionPath).document()
            } else {
                db
                    .collection(
                        collectionPath,
                    ).document(docId)
            }
        docRef
            .set(data, SetOptions.merge()) // Merge updates or add new document
            .addOnSuccessListener {
                onSuccess()
            }.addOnFailureListener { exception ->
                onFailure(exception)
            }
    }

    // Update a single field in the document
    open fun updateSingleField(
        docId: String,
        fieldName: String,
        fieldValue: Any,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit,
    ) {
        db
            .collection(collectionPath)
            .document(docId)
            .update(fieldName, fieldValue)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { exception -> onFailure(exception) }
    }

    // Get a single field value from the document
    open fun getSingleField(
        docId: String,
        fieldName: String,
        onSuccess: (Any) -> Unit,
        onFailure: (Exception) -> Unit,
    ) {
        db
            .collection(collectionPath)
            .document(docId)
            .get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val fieldValue = document.get(fieldName)
                    if (fieldValue != null) {
                        onSuccess(fieldValue)
                    }
                } else {
                    onFailure(Exception("No such document"))
                }
            }.addOnFailureListener { exception -> onFailure(exception) }
    }

    // Delete a document from the collection
    open fun deleteDocument(
        docId: String,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit,
    ) {
        db
            .collection(collectionPath)
            .document(docId)
            .delete()
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { exception -> onFailure(exception) }
    }

    // Abstract function to get the class type for Firestore to use
    abstract fun dataClass(): Class<T>
}
