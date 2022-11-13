package com.tutorial.newbostonbank.service

import com.tutorial.newbostonbank.datasource.BankDataSource
import com.tutorial.newbostonbank.model.Bank
import org.springframework.stereotype.Service

/**
 * A service bean for our Bank entity
 */
@Service
class BankService(private val dataSource: BankDataSource) {

    /**
     * Returns a collection with all the banks in its data source
     */
    fun getBanks(): Collection<Bank> = dataSource.retrieveBanks()

    /**
     * Returns a bank matching the given accountNumber.
     * @param accountNumber a String representing the desired account number.
     */
    fun getBankWithAccountNumber(accountNumber: String): Bank = dataSource.retrieveBank(accountNumber)

    /**
     * Creates a new Bank from the received data
     * @param bank the bank data to be created.
     */
    fun addBank(bank: Bank): Bank = dataSource.createBank(bank)

    /**
     * Updates a Bank using the provided data
     * @param bank the new Bank data.
     */
    fun updateBank(bank: Bank): Bank = dataSource.updateBank(bank)

    /**
     * Deletes a Bank with the given account number
     * @param accountNumber the account number of the Bank to be deleted.
     */
    fun deleteBank(accountNumber: String) = dataSource.deleteBank(accountNumber)

}