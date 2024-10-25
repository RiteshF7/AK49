package com.trex.rexnetwork.domain.firebasecore

data class Shop(
    val ownerName: String = "",
    val shopName: String = "",
    val dealerCode:String ="",
    val tokenBalance: Int = 10,
    val activeDevicesCount: Int = 0
)


class ShopFirestore : FirestoreBase<Shop>("shops") {

    override fun dataClass(): Class<Shop> {
        return Shop::class.java
    }


    // get shop by id
    fun getShopById(shopId: String, onSuccess: (Shop) -> Unit, onFailure: (Exception) -> Unit) {
        getDocument(shopId, dataClass(), onSuccess, onFailure)
    }


    // Create or update a shop
    fun createOrUpdateShop(
        shopId: String? = null,
        shop: Shop,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        addOrUpdateDocument(shopId, shop, onSuccess, onFailure)
    }

    // Update token balance
    fun updateTokenBalance(
        shopId: String,
        newBalance: Int,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        updateSingleField(shopId, "tokenBalance", newBalance, onSuccess, onFailure)
    }

    // Delete a shop
    fun deleteShop(shopId: String, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        deleteDocument(shopId, onSuccess, onFailure)
    }
}




