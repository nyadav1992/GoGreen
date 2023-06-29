package com.example.gogreen

import android.app.Application
import com.example.gogreen.utils.Preferences
import com.walletconnect.walletconnectv2.WalletConnectClient
import com.walletconnect.walletconnectv2.client.ClientTypes
import com.walletconnect.walletconnectv2.client.WalletConnectClientData
import com.walletconnect.walletconnectv2.client.WalletConnectClientListeners
import com.walletconnect.walletconnectv2.client.WalletConnectClientListeners.*
import com.walletconnect.walletconnectv2.common.AppMetaData
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@HiltAndroidApp
class GoGreenApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        Preferences.initSharedPreferences(this)

        MainScope().launch {
            delay(4000)
        }

        initializeWalletConnect()
    }

    private fun initializeWalletConnect() {


//        val initializeParams = ClientTypes.InitialParams(application = application, projectId = "project id", appMetaData = appMetaData)

        val projectId = getString(R.string.project_id)
        val relayUrl = "relay.walletconnect.com"
        val serverUrl = "wss://$relayUrl?projectId=${projectId}"

/*        val appMetaData = Core.Model.AppMetaData(
            name = "Kotlin Wallet",
            description = "Kotlin Wallet Implementation",
            url = "kotlin.wallet.walletconnect.com",
            icons = listOf("https://raw.githubusercontent.com/WalletConnect/walletconnect-assets/master/Icon/Gradient/Icon.png"),
            redirect = "kotlin-web3wallet:/request"
        )*/

/*        CoreClient.initialize(
            relayServerUrl = serverUrl,
            connectionType = ConnectionType.AUTOMATIC,
            application = this,
            metaData = appMetaData
        ) { error ->
            println(error.toString())
//            Log.e(tag(this), error.throwable.stackTraceToString())
        }*/

        val appMetaData = AppMetaData(
            name = "Kotlin Wallet",
            description = "Kotlin Wallet Implementation",
            url = "https://walletconnect.com/",
            icons = listOf("https://raw.githubusercontent.com/WalletConnect/walletconnect-assets/master/Icon/Gradient/Icon.png"),
        )

        val initializeParams = ClientTypes.InitialParams(
            this,
            true,
            "walletconnect",
            getString(R.string.project_id),
            true,
            appMetaData)

        WalletConnectClient.initialize(initializeParams)

        val pairParams = ClientTypes.PairParams("wc:...")

        val pairListener = object: Pairing {
            override fun onSuccess(settledPairing: WalletConnectClientData.SettledPairing) {
                println()
            }
            override fun onError(error: Throwable) {
                println()
            }
        }
        WalletConnectClient.pair(pairParams, pairListener)

/*        Web3Wallet.initialize(Wallet.Params.Init(core = CoreClient)) { error ->
//            Log.e(tag(this), error.throwable.stackTraceToString())
            println(error.toString())
        }*/

/*        val walletDelegate = object : Web3Wallet.WalletDelegate {
            override fun onSessionProposal(sessionProposal: Wallet.Model.SessionProposal) {
                println("print test 1 -> " + sessionProposal.name)
                // Triggered when wallet receives the session proposal sent by a Dapp
            }

            override fun onSessionRequest(sessionRequest: Wallet.Model.SessionRequest) {
                println("print test 2 -> " + sessionRequest.chainId)
                // Triggered when a Dapp sends SessionRequest to sign a transaction or a message
            }

            override fun onAuthRequest(authRequest: Wallet.Model.AuthRequest) {
                println("print test 3 -> " + authRequest.id)
                // Triggered when Dapp / Requester makes an authorization request
            }

            override fun onSessionDelete(sessionDelete: Wallet.Model.SessionDelete) {
                println("print test 4 -> " + sessionDelete.toString())
                // Triggered when the session is deleted by the peer
            }

            override fun onSessionSettleResponse(settleSessionResponse: Wallet.Model.SettledSessionResponse) {
                println("print test 5 -> " + settleSessionResponse.toString())
                // Triggered when wallet receives the session settlement response from Dapp
            }

            override fun onSessionUpdateResponse(sessionUpdateResponse: Wallet.Model.SessionUpdateResponse) {
                println("print test 6 -> " + sessionUpdateResponse.toString())
                // Triggered when wallet receives the session update response from Dapp
            }

            override fun onConnectionStateChange(state: Wallet.Model.ConnectionState) {
                println("print test 7 -> " + state.isAvailable)
                //Triggered whenever the connection state is changed
            }

            override fun onError(error: Wallet.Model.Error) {
                println("print test 8 -> " + error.toString())
                // Triggered whenever there is an issue inside the SDK
            }
        }
        Web3Wallet.setWalletDelegate(walletDelegate)*/

    }

}