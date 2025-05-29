package adb

/**
 * Example of pair or connect info "IP_ADDRESS:PORT:
 * "192.168.0.247:38361"
 */
internal data class MdnsServices(
    val pairingInfo: String,
    val connectingInfo: String,
)
