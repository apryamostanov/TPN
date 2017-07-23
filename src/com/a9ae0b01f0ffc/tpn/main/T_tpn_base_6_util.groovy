package com.a9ae0b01f0ffc.tpn.main

import com.a9ae0b01f0ffc.black_box.annotations.I_black_box
import com.a9ae0b01f0ffc.black_box.annotations.I_fix_variable_scopes
import com.a9ae0b01f0ffc.black_box.implementation.T_logger
import com.a9ae0b01f0ffc.black_box.main.T_logging_base_5_context
import com.a9ae0b01f0ffc.tpn.implementation.T_tpn_standard_message_format
import groovy.sql.Sql
import groovy.util.slurpersupport.GPathResult

@I_fix_variable_scopes
class T_tpn_base_6_util extends T_tpn_base_5_context {

    @I_black_box("error")
    static void sql_update(String i_sql_string, Object... i_bind_variables = GC_SKIPPED_ARGS) {
        l().log_send_sql("update", i_sql_string, i_bind_variables)
        if (method_arguments_present(i_bind_variables)) {
            get_sql().executeUpdate(i_sql_string, i_bind_variables)
        } else {
            get_sql().executeUpdate(i_sql_string)
        }
        l().log_receive_sql("updated", get_sql().getUpdateCount())
    }

    @I_black_box("error")
    static T_tpn_standard_message_format parse_payload(String i_payload) {
        T_tpn_standard_message_format l_tpn_standard_message_format = new T_tpn_standard_message_format()
        GPathResult l_gpath_result = new XmlSlurper().parseText(i_payload)
        l_tpn_standard_message_format.productID = l_gpath_result?.Body?.TransactionNotificationRequest?.product?.productID?.text()
        l_tpn_standard_message_format.productName = l_gpath_result?.Body?.TransactionNotificationRequest?.product?.productName?.text()
        l_tpn_standard_message_format.programManager = l_gpath_result?.Body?.TransactionNotificationRequest?.product?.programManager?.text()
        l_tpn_standard_message_format.productCategory = l_gpath_result?.Body?.TransactionNotificationRequest?.product?.productCategory?.text()
        l_tpn_standard_message_format.subProductType = l_gpath_result?.Body?.TransactionNotificationRequest?.product?.subProductType?.text()
        l_tpn_standard_message_format.sourceBusinessName = l_gpath_result?.Body?.TransactionNotificationRequest?.businessAccount?.sourceBusinessName?.text()
        l_tpn_standard_message_format.sourceBusinessAccountNumber = l_gpath_result?.Body?.TransactionNotificationRequest?.businessAccount?.sourceBusinessAccountNumber?.text()
        l_tpn_standard_message_format.businessName = l_gpath_result?.Body?.TransactionNotificationRequest?.businessAccount?.businessName?.text()
        l_tpn_standard_message_format.businessAccountNumber = l_gpath_result?.Body?.TransactionNotificationRequest?.businessAccount?.businessAccountNumber?.text()
        l_tpn_standard_message_format.sourceCardNumber = l_gpath_result?.Body?.TransactionNotificationRequest?.card?.sourceCardNumber?.text()
        l_tpn_standard_message_format.sourceProxyNumber = l_gpath_result?.Body?.TransactionNotificationRequest?.card?.sourceProxyNumber?.text()
        l_tpn_standard_message_format.cardNumber = l_gpath_result?.Body?.TransactionNotificationRequest?.card?.cardNumber?.text()
        l_tpn_standard_message_format.proxyNumber = l_gpath_result?.Body?.TransactionNotificationRequest?.card?.proxyNumber?.text()
        l_tpn_standard_message_format.cardSequenceNumber = l_gpath_result?.Body?.TransactionNotificationRequest?.card?.cardSequenceNumber?.text()
        l_tpn_standard_message_format.sourceAccountNumber = l_gpath_result?.Body?.TransactionNotificationRequest?.account?.sourceAccountNumber?.text()
        l_tpn_standard_message_format.sourceAccountCurrency = l_gpath_result?.Body?.TransactionNotificationRequest?.account?.sourceAccountCurrency?.text()
        l_tpn_standard_message_format.sourceAccountBalanceInt = l_gpath_result?.Body?.TransactionNotificationRequest?.account?.sourceAccountBalanceInt?.text()
        l_tpn_standard_message_format.sourceAccountBalanceDec = l_gpath_result?.Body?.TransactionNotificationRequest?.account?.sourceAccountBalanceDec?.text()
        l_tpn_standard_message_format.populated = l_gpath_result?.Body?.TransactionNotificationRequest?.account?.populated?.text()
        l_tpn_standard_message_format.AccountNumber = l_gpath_result?.Body?.TransactionNotificationRequest?.account?.AccountNumber?.text()
        l_tpn_standard_message_format.AccountCurrency = l_gpath_result?.Body?.TransactionNotificationRequest?.account?.AccountCurrency?.text()
        l_tpn_standard_message_format.AccountBalanceInt = l_gpath_result?.Body?.TransactionNotificationRequest?.account?.AccountBalanceInt?.text()
        l_tpn_standard_message_format.AccountBalanceDec = l_gpath_result?.Body?.TransactionNotificationRequest?.account?.AccountBalanceDec?.text()
        l_tpn_standard_message_format.transactionId = l_gpath_result?.Body?.TransactionNotificationRequest?.transaction?.transactionId?.text()
        l_tpn_standard_message_format.transactionCode = l_gpath_result?.Body?.TransactionNotificationRequest?.transaction?.transactionCode?.text()
        l_tpn_standard_message_format.transactionDescription = l_gpath_result?.Body?.TransactionNotificationRequest?.transaction?.transactionDescription?.text()
        l_tpn_standard_message_format.transactionTimeStamp = l_gpath_result?.Body?.TransactionNotificationRequest?.transaction?.transactionTimeStamp?.text()
        l_tpn_standard_message_format.transactionLocalDate = l_gpath_result?.Body?.TransactionNotificationRequest?.transaction?.transactionLocalDate?.text()
        l_tpn_standard_message_format.transactionLocalTime = l_gpath_result?.Body?.TransactionNotificationRequest?.transaction?.transactionLocalTime?.text()
        l_tpn_standard_message_format.transactionLifecycleState = l_gpath_result?.Body?.TransactionNotificationRequest?.transaction?.transactionLifecycleState?.text()
        l_tpn_standard_message_format.transactionOriginalId = l_gpath_result?.Body?.TransactionNotificationRequest?.transaction?.transactionOriginalId?.text()
        l_tpn_standard_message_format.messageTypeIdentifier = l_gpath_result?.Body?.TransactionNotificationRequest?.transaction?.messageTypeIdentifier?.text()
        l_tpn_standard_message_format.transactionResponseCode = l_gpath_result?.Body?.TransactionNotificationRequest?.transaction?.transactionResponseCode?.text()
        l_tpn_standard_message_format.debitCreditFlag = l_gpath_result?.Body?.TransactionNotificationRequest?.transaction?.debitCreditFlag?.text()
        l_tpn_standard_message_format.logicModule = l_gpath_result?.Body?.TransactionNotificationRequest?.transaction?.logicModule?.text()
        l_tpn_standard_message_format.transactionPostingDate = l_gpath_result?.Body?.TransactionNotificationRequest?.transaction?.transactionPostingDate?.text()
        l_tpn_standard_message_format.transactionPostingTime = l_gpath_result?.Body?.TransactionNotificationRequest?.transaction?.transactionPostingTime?.text()
        l_tpn_standard_message_format.postingFlag = l_gpath_result?.Body?.TransactionNotificationRequest?.transaction?.postingFlag?.text()
        l_tpn_standard_message_format.postingReference = l_gpath_result?.Body?.TransactionNotificationRequest?.transaction?.postingReference?.text()
        l_tpn_standard_message_format.postingTransactionSource = l_gpath_result?.Body?.TransactionNotificationRequest?.transaction?.postingTransactionSource?.text()
        l_tpn_standard_message_format.originalTransactionSource = l_gpath_result?.Body?.TransactionNotificationRequest?.transaction?.originalTransactionSource?.text()
        l_tpn_standard_message_format.networkName = l_gpath_result?.Body?.TransactionNotificationRequest?.network?.networkName?.text()
        l_tpn_standard_message_format.networkTransactionId = l_gpath_result?.Body?.TransactionNotificationRequest?.network?.networkTransactionId?.text()
        l_tpn_standard_message_format.networkProcessingCode = l_gpath_result?.Body?.TransactionNotificationRequest?.network?.networkProcessingCode?.text()
        l_tpn_standard_message_format.networkRRN = l_gpath_result?.Body?.TransactionNotificationRequest?.network?.networkRRN?.text()
        l_tpn_standard_message_format.networkSTAN = l_gpath_result?.Body?.TransactionNotificationRequest?.network?.networkSTAN?.text()
        l_tpn_standard_message_format.networkResponseCode = l_gpath_result?.Body?.TransactionNotificationRequest?.network?.networkResponseCode?.text()
        l_tpn_standard_message_format.sourcePostedCurrency = l_gpath_result?.Body?.TransactionNotificationRequest?.amounts?.sourcePostedCurrency?.text()
        l_tpn_standard_message_format.sourcePostedAmountInt = l_gpath_result?.Body?.TransactionNotificationRequest?.amounts?.sourcePostedAmountInt?.text()
        l_tpn_standard_message_format.sourcePostedAmountDec = l_gpath_result?.Body?.TransactionNotificationRequest?.amounts?.sourcePostedAmountDec?.text()
        l_tpn_standard_message_format.transactionCurrency = l_gpath_result?.Body?.TransactionNotificationRequest?.amounts?.transactionCurrency?.text()
        l_tpn_standard_message_format.transactionAmountInt = l_gpath_result?.Body?.TransactionNotificationRequest?.amounts?.transactionAmountInt?.text()
        l_tpn_standard_message_format.transactionAmountDec = l_gpath_result?.Body?.TransactionNotificationRequest?.amounts?.transactionAmountDec?.text()
        l_tpn_standard_message_format.billingCurrency = l_gpath_result?.Body?.TransactionNotificationRequest?.amounts?.billingCurrency?.text()
        l_tpn_standard_message_format.billingAmountInt = l_gpath_result?.Body?.TransactionNotificationRequest?.amounts?.billingAmountInt?.text()
        l_tpn_standard_message_format.billingAmountDec = l_gpath_result?.Body?.TransactionNotificationRequest?.amounts?.billingAmountDec?.text()
        l_tpn_standard_message_format.postedCurrency = l_gpath_result?.Body?.TransactionNotificationRequest?.amounts?.postedCurrency?.text()
        l_tpn_standard_message_format.postedAmountInt = l_gpath_result?.Body?.TransactionNotificationRequest?.amounts?.postedAmountInt?.text()
        l_tpn_standard_message_format.postedAmountDec = l_gpath_result?.Body?.TransactionNotificationRequest?.amounts?.postedAmountDec?.text()
        l_tpn_standard_message_format.additionalAmountCurrency = l_gpath_result?.Body?.TransactionNotificationRequest?.amounts?.additionalAmountCurrency?.text()
        l_tpn_standard_message_format.additionalAmountInt = l_gpath_result?.Body?.TransactionNotificationRequest?.amounts?.additionalAmountInt?.text()
        l_tpn_standard_message_format.additionalAmountDec = l_gpath_result?.Body?.TransactionNotificationRequest?.amounts?.additionalAmountDec?.text()
        l_tpn_standard_message_format.feeName = l_gpath_result?.Body?.TransactionNotificationRequest?.fees?.feeName?.text()
        l_tpn_standard_message_format.feeAmountCurrency = l_gpath_result?.Body?.TransactionNotificationRequest?.fees?.feeAmountCurrency?.text()
        l_tpn_standard_message_format.feeAmountInt = l_gpath_result?.Body?.TransactionNotificationRequest?.fees?.feeAmountInt?.text()
        l_tpn_standard_message_format.feeAmountDec = l_gpath_result?.Body?.TransactionNotificationRequest?.fees?.feeAmountDec?.text()
        l_tpn_standard_message_format.feeTransactionId = l_gpath_result?.Body?.TransactionNotificationRequest?.fees?.feeTransactionId?.text()
        l_tpn_standard_message_format.acquirerId = l_gpath_result?.Body?.TransactionNotificationRequest?.pos_merchant?.acquirerId?.text()
        l_tpn_standard_message_format.acquirerCountry = l_gpath_result?.Body?.TransactionNotificationRequest?.pos_merchant?.acquirerCountry?.text()
        l_tpn_standard_message_format.acquirerInstitutionIDCode = l_gpath_result?.Body?.TransactionNotificationRequest?.pos_merchant?.acquirerInstitutionIDCode?.text()
        l_tpn_standard_message_format.storeNumber = l_gpath_result?.Body?.TransactionNotificationRequest?.pos_merchant?.storeNumber?.text()
        l_tpn_standard_message_format.terminalId = l_gpath_result?.Body?.TransactionNotificationRequest?.pos_merchant?.terminalId?.text()
        l_tpn_standard_message_format.terminalType = l_gpath_result?.Body?.TransactionNotificationRequest?.pos_merchant?.terminalType?.text()
        l_tpn_standard_message_format.terminalCategory = l_gpath_result?.Body?.TransactionNotificationRequest?.pos_merchant?.terminalCategory?.text()
        l_tpn_standard_message_format.terminalEntry = l_gpath_result?.Body?.TransactionNotificationRequest?.pos_merchant?.terminalEntry?.text()
        l_tpn_standard_message_format.cardAcceptorIDCode = l_gpath_result?.Body?.TransactionNotificationRequest?.pos_merchant?.cardAcceptorIDCode?.text()
        l_tpn_standard_message_format.cardAcceptorterminalIDCode = l_gpath_result?.Body?.TransactionNotificationRequest?.pos_merchant?.cardAcceptorterminalIDCode?.text()
        l_tpn_standard_message_format.merchantId = l_gpath_result?.Body?.TransactionNotificationRequest?.pos_merchant?.merchantId?.text()
        l_tpn_standard_message_format.merchantCode = l_gpath_result?.Body?.TransactionNotificationRequest?.pos_merchant?.merchantCode?.text()
        l_tpn_standard_message_format.merchantName = l_gpath_result?.Body?.TransactionNotificationRequest?.pos_merchant?.merchantName?.text()
        l_tpn_standard_message_format.merchantAddress = l_gpath_result?.Body?.TransactionNotificationRequest?.pos_merchant?.merchantAddress?.text()
        l_tpn_standard_message_format.merchantCity = l_gpath_result?.Body?.TransactionNotificationRequest?.pos_merchant?.merchantCity?.text()
        l_tpn_standard_message_format.merchantCountry = l_gpath_result?.Body?.TransactionNotificationRequest?.pos_merchant?.merchantCountry?.text()
        l_tpn_standard_message_format.merchantType = l_gpath_result?.Body?.TransactionNotificationRequest?.pos_merchant?.merchantType?.text()
        l_tpn_standard_message_format.posEntryMode = l_gpath_result?.Body?.TransactionNotificationRequest?.pos_merchant?.posEntryMode?.text()
        l_tpn_standard_message_format.posConditionCode = l_gpath_result?.Body?.TransactionNotificationRequest?.pos_merchant?.posConditionCode?.text()
        l_tpn_standard_message_format.posPINCaptureCode = l_gpath_result?.Body?.TransactionNotificationRequest?.pos_merchant?.posPINCaptureCode?.text()
        l_tpn_standard_message_format.nationalPOSGeographicData = l_gpath_result?.Body?.TransactionNotificationRequest?.pos_merchant?.nationalPOSGeographicData?.text()
        l_tpn_standard_message_format.chipCondition = l_gpath_result?.Body?.TransactionNotificationRequest?.pos_merchant?.chipCondition?.text()
        l_tpn_standard_message_format.chipTransactionFlag = l_gpath_result?.Body?.TransactionNotificationRequest?.pos_merchant?.chipTransactionFlag?.text()
        l_tpn_standard_message_format.paymentIndicator = l_gpath_result?.Body?.TransactionNotificationRequest?.pos_merchant?.paymentIndicator?.text()
        l_tpn_standard_message_format.cardholderId = l_gpath_result?.Body?.TransactionNotificationRequest?.pos_merchant?.cardholderId?.text()
        l_tpn_standard_message_format.availableBalance = l_gpath_result?.Body?.TransactionNotificationRequest?.additionalData?.availableBalance?.text()
        l_tpn_standard_message_format.currentBalance = l_gpath_result?.Body?.TransactionNotificationRequest?.additionalData?.currentBalance?.text()
        l_tpn_standard_message_format.lastCreditAmount = l_gpath_result?.Body?.TransactionNotificationRequest?.additionalData?.lastCreditAmount?.text()
        l_tpn_standard_message_format.lastCreditDate = l_gpath_result?.Body?.TransactionNotificationRequest?.additionalData?.lastCreditDate?.text()
        l_tpn_standard_message_format.lastTransactionDate = l_gpath_result?.Body?.TransactionNotificationRequest?.additionalData?.lastTransactionDate?.text()
        l_tpn_standard_message_format.isDeclined = l_gpath_result?.Body?.TransactionNotificationRequest?.additionalData?.isDeclined?.text()
        l_tpn_standard_message_format.transactionType = l_gpath_result?.Body?.TransactionNotificationRequest?.additionalData?.transactionType?.text()
        l_tpn_standard_message_format.settleAmount = l_gpath_result?.Body?.TransactionNotificationRequest?.additionalData?.settleAmount?.text()
        l_tpn_standard_message_format.settleCurrency = l_gpath_result?.Body?.TransactionNotificationRequest?.additionalData?.settleCurrency?.text()
        return l_tpn_standard_message_format
    }

    @I_black_box("error")
    static void commit() {
        l().log_send_sql("commit")
        get_sql().commit()
        l().log_receive_sql("commit_finished")
    }

    @I_black_box("error")
    static Sql get_sql() {
        if ((System.currentTimeMillis() - get_context().p_sql_last_init_time_millis) >= new Long(c().GC_SQL_SESSION_REFRESH_INTERVAL_MILLISECONDS)) {
            init_sql()
        }
        //TODO: healthcheck here with handling of CommunicationsException
        return get_context().p_sql
    }

    @I_black_box("error")
    static T_logger l() {
        return T_logging_base_5_context.l()
    }

}
