package com.a9ae0b01f0ffc.tpn.implementation

import com.a9ae0b01f0ffc.black_box.annotations.I_black_box
import com.a9ae0b01f0ffc.black_box.annotations.I_fix_variable_scopes
import com.a9ae0b01f0ffc.middleware.Interfaces.I_http_conversion_module
import com.a9ae0b01f0ffc.middleware.Interfaces.I_http_message
import com.a9ae0b01f0ffc.middleware.main.T_middleware_base_6_util
import groovy.transform.CompileStatic
import groovy.transform.ToString
import groovy.util.slurpersupport.GPathResult
import groovy.util.slurpersupport.NodeChild
import groovy.xml.XmlUtil
import org.w3c.dom.Element

@I_fix_variable_scopes
@ToString(includeNames = true, includeFields = true, includeSuper = false)
class T_wdip2gfs_conversion_module extends T_middleware_base_6_util implements I_http_conversion_module {

    @I_black_box("error")
    /*full previously*/
    @Override
    I_http_message convert_http_message(I_http_message i_http_message) {
        I_http_message l_result_http_message = i_http_message
        l_result_http_message.set_payload(make_payload_post(i_http_message.get_payload()))
        return l_result_http_message
    }

    @I_black_box("error")
    /*orig*/
    static ArrayList<NodeChild> find_element(GPathResult i_gpath_result, String i_element_name) {
        return i_gpath_result.depthFirst().findAll { l_element -> l_element.name() == i_element_name }
    }

    @I_black_box("error")
    /*full previously*/
    static String tag(GPathResult i_gpath_result, String i_element_name, String i_name_space = GC_EMPTY_STRING) {
        ArrayList<NodeChild> l_node_children = find_element(i_gpath_result, i_element_name)
        String l_prefix = GC_EMPTY_STRING
        if (i_name_space != GC_EMPTY_STRING) {
            l_prefix += i_name_space + ":"
        }
        if (l_node_children.isEmpty()) {
            return l_prefix + i_element_name.substring(i_element_name.lastIndexOf(".") + 1) + " i:nil=\"true\"/"
        } else {
            GPathResult l_gpath_result = l_node_children.first()
            if (l_gpath_result.text() != GC_EMPTY_STRING) {
                return l_prefix + i_element_name.substring(i_element_name.lastIndexOf(".") + 1) + ">" + l_gpath_result.text() + "</" + l_prefix + i_element_name.substring(i_element_name.lastIndexOf(".") + 1)
            } else {
                return l_prefix + i_element_name.substring(i_element_name.lastIndexOf(".") + 1) + " i:nil=\"true\"/"
            }
        }
    }

    @I_black_box("error")
    /*full previously*/
    static String tag(HashMap<String, String[]> i_parameter_map, String i_element_name, String i_name_space = GC_EMPTY_STRING) {
        String l_parameter_value = i_parameter_map.get(i_element_name)?.getAt(GC_FIRST_INDEX)
        String l_prefix = GC_EMPTY_STRING
        if (i_name_space != GC_EMPTY_STRING) {
            l_prefix += i_name_space + ":"
        }
        if (is_not_null(l_parameter_value)) {
            return l_prefix + i_element_name.substring(i_element_name.lastIndexOf(".") + 1) + ">" + l_parameter_value + "</" + l_prefix + i_element_name.substring(i_element_name.lastIndexOf(".") + 1)
        } else {
            return l_prefix + i_element_name.substring(i_element_name.lastIndexOf(".") + 1) + " i:nil=\"true\"/"
        }
    }

    @I_black_box("error")
    static String make_payload_post(String i_xml_payload_orig) {
        String l_decorated_original_xml = "<root_added_by_tpn>" + i_xml_payload_orig + "</root_added_by_tpn>"
        GPathResult l_gpath_result = new XmlSlurper().parseText(l_decorated_original_xml)
        String l_payload
        //Below code is only for non-scheme transactions (i.e. Load/Unload/Fees/etc...)
        if (l_gpath_result.children().size() == GC_ONE_ONLY) {//Aftab xml
            l_gpath_result = l_gpath_result.children()[GC_FIRST_INDEX] as GPathResult
            if (l_gpath_result.Body.TransactionNotificationRequest.children().size() == GC_EMPTY_SIZE) {
                l().log_warning(s.Potentially_non_WDIP_original_message_structure)
                return i_xml_payload_orig
            }
            String l_available_balance = l_gpath_result.Body.TransactionNotificationRequest.account.AccountBalanceInt.text() + "." + l_gpath_result.Body.TransactionNotificationRequest.account.AccountBalanceDec.text()
            String l_AccountNumber = l_gpath_result.Body.TransactionNotificationRequest.account.AccountNumber.text()
            String l_BillingCurrencyCode = currency_digits(l_gpath_result.Body.TransactionNotificationRequest.amounts.billingCurrency.text().padLeft(GC_THREE_CHARS, GC_ZERO_CHAR))
            String l_CMTTranType = l_gpath_result.Body.TransactionNotificationRequest.transaction.logicModule.text()
            String l_CardAcceptorNameLocation = "GFS 8 Marina View, 40-01, Asia Sqaure Tower 1. 018960"
//l_gpath_result.Body.TransactionNotificationRequest.pos_merchant.merchantName.text()
            String l_CardMasking = l_gpath_result.Body.TransactionNotificationRequest.card.cardNumber.text().replace("X", "*")
            String l_CardNumber4Digits = last_chars(l_gpath_result.Body.TransactionNotificationRequest.card.cardNumber.text(), GC_FOUR_CHARS)
            String l_Current_Balance = l_gpath_result.Body.TransactionNotificationRequest.account.AccountBalanceInt.text() + "." + l_gpath_result.Body.TransactionNotificationRequest.account.AccountBalanceDec.text()
            String l_CustomAcctID
            String l_IsDeclined = (l_gpath_result.Body.TransactionNotificationRequest.transaction.postingReference.text() == "Transaction posted successfully" ? "no" : "yes")
            String l_MerchantCity = "Singapore"
//l_gpath_result.Body.TransactionNotificationRequest.pos_merchant.merchantCity.text()
            String l_PostTime = d2s(s2d(l_gpath_result.Body.TransactionNotificationRequest.transaction.transactionLocalDate.text(), "dd-MMM-yyyy"), "yyyy-MM-dd") + "T" + l_gpath_result.Body.TransactionNotificationRequest.transaction.transactionLocalTime.text()
            String l_PostingRef = (l_IsDeclined == "no" ? "Approved" : ("Declined - " + l_gpath_result.Body.TransactionNotificationRequest.transaction.postingReference.text()))
            String l_ProductBin = l_gpath_result.Body.TransactionNotificationRequest.card.cardNumber.text().substring(0, 6)
            String l_ProxyNumber = l_gpath_result.Body.TransactionNotificationRequest.card.proxyNumber.text()
            String l_SpendCategory = "Miscellaneous"
            String l_TranId = l_gpath_result.Body.TransactionNotificationRequest.transaction.transactionId.text()
            String l_TranTime = d2s(s2d(l_gpath_result.Body.TransactionNotificationRequest.transaction.transactionLocalDate.text(), "dd-MMM-yyyy"), "yyyy-MM-dd") + "T" + l_gpath_result.Body.TransactionNotificationRequest.transaction.transactionLocalTime.text()
            String l_TransactionAmount = l_gpath_result.Body.TransactionNotificationRequest.amounts.transactionAmountInt.text() + "." + l_gpath_result.Body.TransactionNotificationRequest.amounts.transactionAmountDec.text()
            String l_TransactionCurrencyCode = currency_digits(l_gpath_result.Body.TransactionNotificationRequest.amounts.transactionCurrency.text().padLeft(GC_THREE_CHARS, GC_ZERO_CHAR))
            String l_TransactionDescription = l_gpath_result.Body.TransactionNotificationRequest.transaction.transactionDescription.text()
            String l_TransactionReason
            String l_TransactionType
            String l_TxnSource
            String l_TypeOfTran
            String l_MCC = "5999"
//l_gpath_result.Body.TransactionNotificationRequest.pos_merchant.terminalCategory.text()
            String l_MerchantName = "GFS"
//l_gpath_result.Body.TransactionNotificationRequest.pos_merchant.merchantName.text()
            String l_MerchantCountry = "SG"
//l_gpath_result.Body.TransactionNotificationRequest.pos_merchant.merchantCountry.text()
            String l_MerchantPostalCode = "018960"
            String l_MerchantAddress = "8 Marina View, 40-01, Asia Sqaure Tower 1. 018960"
//l_gpath_result.Body.TransactionNotificationRequest.pos_merchant.merchantAddress.text()
            String l_BillingAmount = l_gpath_result.Body.TransactionNotificationRequest.amounts.billingAmountInt.text() + "." + l_gpath_result.Body.TransactionNotificationRequest.amounts.billingAmountDec.text()
            String l_SettleAmount
            String l_SettleCurrency
            String l_TerminalID = "GFS"
//l_gpath_result.Body.TransactionNotificationRequest.pos_merchant.terminalId.text()
            String l_TerminalType
            String l_TraceNum = ""//l_gpath_result.Body.TransactionNotificationRequest.network.networkSTAN.text()
            String l_PostedAmount = l_gpath_result.Body.TransactionNotificationRequest.amounts.postedAmountInt.text() + "." + l_gpath_result.Body.TransactionNotificationRequest.amounts.postedAmountDec.text()
            String l_PostedCurrency = currency_digits(l_gpath_result.Body.TransactionNotificationRequest.amounts.postedCurrency.text().padLeft(GC_THREE_CHARS, GC_ZERO_CHAR))
            String l_MerchantId = "GFS"
//l_gpath_result.Body.TransactionNotificationRequest.pos_merchant.merchantId.text()
            String l_RRN = ""//l_gpath_result.Body.TransactionNotificationRequest.network.networkRRN.text()
            String l_AcquirerCountry = "702"
//l_gpath_result.Body.TransactionNotificationRequest.pos_merchant.acquirerCountry.text()
            String l_AcquirerID = "426437"
//l_gpath_result.Body.TransactionNotificationRequest.pos_merchant.acquirerId.text()
            l_payload = """<s:Envelope xmlns:s="http://schemas.xmlsoap.org/soap/envelope/">
   <s:Body>
      <TranHistNotification xmlns="http://tempuri.org/">
           <TranHistNotificationRequest xmlns:a="http://schemas.datacontract.org/2004/07/WDNotifyServices.DTO" xmlns:i="http://www.w3.org/2001/XMLSchema-instance">
            <a:AvailableBalance>$l_available_balance</a:AvailableBalance>
            <a:CurrentBalance>$l_Current_Balance</a:CurrentBalance>
            <a:FilterResultCount>1</a:FilterResultCount>
            <a:LastCreditAmount/>
            <a:LastCreditDate/>
            <a:LastTransactionDate/>
            <a:TransactionHistoryList>
               <a:TransactionsHistory>
                  <a:AccountNumber>$l_AccountNumber</a:AccountNumber>
                  <a:BillingCurrencyCode>$l_BillingCurrencyCode</a:BillingCurrencyCode>
                  <a:CMTTranType>$l_CMTTranType</a:CMTTranType>
                  <a:CardAcceptorNameLocation>$l_CardAcceptorNameLocation</a:CardAcceptorNameLocation>
                  <a:CardMasking>$l_CardMasking</a:CardMasking>
                  <a:CardNumber4Digits>$l_CardNumber4Digits</a:CardNumber4Digits>
                  <a:Current_Balance>$l_Current_Balance</a:Current_Balance>
                  <a:CustomAcctID/>
                  <a:IsDeclined>$l_IsDeclined</a:IsDeclined>
                  <a:MerchantCity>$l_MerchantCity</a:MerchantCity>
                  <a:PostTime>$l_PostTime</a:PostTime>
                  <a:PostingRef>$l_PostingRef</a:PostingRef>
                  <a:ProductBin>$l_ProductBin</a:ProductBin>
                  <a:ProxyNumber>$l_ProxyNumber</a:ProxyNumber>
                  <a:SpendCategory>$l_SpendCategory</a:SpendCategory>
                  <a:TranId>$l_TranId</a:TranId>
                  <a:TranTime>$l_TranTime</a:TranTime>
                  <a:TransactionAmount>$l_TransactionAmount</a:TransactionAmount>
                  <a:TransactionCurrencyCode>$l_TransactionCurrencyCode</a:TransactionCurrencyCode>
                  <a:TransactionDescription>$l_TransactionDescription</a:TransactionDescription>
                  <a:TransactionReason/>
                  <a:TransactionType>Settled</a:TransactionType>
                  <a:TxnSource>Visa SMS</a:TxnSource>
                  <a:TypeOfTran>Tran</a:TypeOfTran>
                  <a:MCC>$l_MCC</a:MCC>
                  <a:MerchantName>$l_MerchantName</a:MerchantName>
                  <a:MerchantCountry>$l_MerchantCountry</a:MerchantCountry>
                  <a:MerchantPostalCode/>
                  <a:MerchantAddress>$l_MerchantAddress</a:MerchantAddress>
                  <a:BillingAmount>$l_BillingAmount</a:BillingAmount>
                  <a:SettleAmount/>
                  <a:SettleCurrency/>
                  <a:TerminalID>$l_TerminalID</a:TerminalID>
                  <a:TerminalType>E</a:TerminalType>
                  <a:TraceNum>$l_TraceNum</a:TraceNum>
                  <a:PostedAmount>$l_PostedAmount</a:PostedAmount>
                  <a:PostedCurrency>$l_PostedCurrency</a:PostedCurrency>
                  <a:MerchantId>$l_MerchantId</a:MerchantId>
                  <a:RRN>$l_RRN</a:RRN>
                  <a:AcquirerCountry>$l_AcquirerCountry</a:AcquirerCountry>
                  <a:AcquirerID>$l_AcquirerID</a:AcquirerID>
               </a:TransactionsHistory>
            </a:TransactionHistoryList>
            <a:UniqueID/>
            <a:UniqueIDflag>0</a:UniqueIDflag>
         </TranHistNotificationRequest>
      </TranHistNotification>
   </s:Body>
</s:Envelope>"""
        } else {
            l_payload = XmlUtil.serialize(l_gpath_result.children()[GC_SECOND_INDEX] as GPathResult) //Satya xml
        }
        return l_payload
    }

    @I_black_box("error")
    /*full previously*/
    static String get_service_name(I_http_message i_http_request) {
        String l_service_name = GC_EMPTY_STRING
        URL l_url = new URL(i_http_request.get_url())
        String l_path = l_url.getPath()
        l_service_name = l_path.substring(l_path.lastIndexOf("/") + GC_ONE_CHAR)
        return l_service_name
    }

}
