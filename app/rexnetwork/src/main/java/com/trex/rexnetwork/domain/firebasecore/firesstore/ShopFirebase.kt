package com.trex.rexnetwork.domain.firebasecore.firesstore

import android.util.Log
import com.trex.rexnetwork.data.BaseFirestoreResponse
import kotlinx.parcelize.Parcelize
import kotlin.random.Random

@Parcelize
data class Shop(
    val shopName: String = "",
    val ownerName: String = "",
    val shopCode: String = Random.nextInt(1, 100000).toString(),
    val fcmToken: String = "",
    val tokenBalance: List<String> = listOf(),
) : BaseFirestoreResponse

class ShopFirestore : FirestoreBase<Shop>("shops") {
    override fun dataClass(): Class<Shop> = Shop::class.java

    // get shop by id
    fun getShopById(
        shopId: String,
        onSuccess: (Shop) -> Unit,
        onFailure: (Exception) -> Unit,
    ) {
        getDocument(shopId, dataClass(), onSuccess, onFailure)
    }

    fun getShopFcmToken(
        shopId: String,
        onSuccess: (String) -> Unit,
    ) {
        getShopById(
            shopId,
            { shop -> onSuccess(shop.fcmToken) },
            { error -> Log.e("", "getShopFcmToken: error getting shop from client app!!") },
        )
    }

    fun getTokenBalanceList(
        shopId: String,
        onSuccess: (List<String>) -> Unit,
    ) {
        getSingleField(
            shopId,
            Shop::tokenBalance.name,
            { shop -> onSuccess(shop as List<String>) },
            { error -> Log.e("", "getShopFcmToken: error getting shop from client app!!") },
        )
    }

    // Create or update a shop
    fun createOrUpdateShop(
        shopId: String? = null,
        shop: Shop,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit,
    ) {
        addOrUpdateDocument(shopId, shop, onSuccess, onFailure)
    }

    // Update token balance
    fun updateTokenBalance(
        shopId: String,
        newBalance: Int,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit,
    ) {
        updateSingleField(shopId, "tokenBalance", newBalance, onSuccess, onFailure)
    }

    // Delete a shop
    fun deleteShop(
        shopId: String,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit,
    ) {
        deleteDocument(shopId, onSuccess, onFailure)
    }
}
