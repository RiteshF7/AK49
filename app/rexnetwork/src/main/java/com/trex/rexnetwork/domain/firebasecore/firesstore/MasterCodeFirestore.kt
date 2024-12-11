package com.trex.rexnetwork.domain.firebasecore.firesstore

import com.trex.rexnetwork.data.Actions
import com.trex.rexnetwork.data.ActionsMapper
import com.trex.rexnetwork.data.BaseFirestoreResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.parcelize.Parcelize
import kotlin.random.Random

@Parcelize
data class MasterCode(
    val actions: String = "",
) : BaseFirestoreResponse

class MasterCodeFirestore : FirestoreBase<MasterCode>("mastercode") {
    override fun dataClass(): Class<MasterCode> = MasterCode::class.java

    val newMasterCode = Random.nextInt(0, 100000).toString()

    fun getMasterCodeAction(
        masterCode: String,
        onComplete: (Actions) -> Unit,
        notFound: () -> Unit,
    ) {
        getDocument(masterCode, dataClass(), { masterCodeData ->
            ActionsMapper.fromStringToEnum(masterCodeData.actions)?.let { myAction ->
                onComplete(myAction)
            }
            deleteDocument(masterCode, {
                addOrUpdateDocument(newMasterCode, masterCodeData, {}, {})
            }, {})
        }, { exception ->
            notFound()
        })
    }



    fun genrateInitialData() {
        CoroutineScope(Dispatchers.IO).launch {
            for (action in Actions.values()) {
                val code = Random.nextInt(0, 100000).toString()
                addOrUpdateDocument(code, MasterCode(action.name), {}, {})
            }
        }
    }
}
