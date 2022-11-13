package com.tutorial.newbostonbank.datasource.mock

import com.tutorial.newbostonbank.datasource.BankDataSource
import com.tutorial.newbostonbank.exception.UnprocessableEntityException
import com.tutorial.newbostonbank.exception.ElementNotFoundException
import com.tutorial.newbostonbank.model.Bank
import org.springframework.stereotype.Repository

/**
 * A mock bank data source repository to retrieve some
 */
@Repository
class MockBankDataSource : BankDataSource {
    val banks = mutableListOf(
        Bank("1234", 3.14, 17),
        Bank("1010", 17.0, 0),
        Bank("5678", 0.0, 100)
    )

    override fun retrieveBanks(): Collection<Bank> = banks

    override fun retrieveBank(accountNumber: String): Bank =
        banks.firstOrNull() { bank -> bank.accountNumber == accountNumber }
            ?: throw ElementNotFoundException("Could not find a bank with an account number matching $accountNumber")

    override fun createBank(bank: Bank): Bank {
        if (banks.indexOf(bank) != -1)
            throw UnprocessableEntityException("The bank with account number ${bank.accountNumber} already exists.")

        banks.add(bank)
        return bank
    }

    override fun updateBank(bank: Bank): Bank {
        val bankToBeUpdated = banks.firstOrNull { it.accountNumber == bank.accountNumber }
            ?: throw UnprocessableEntityException("The bank with account number ${bank.accountNumber} not exists.")

        banks.remove(bankToBeUpdated)
        banks.add(bank)

        return bank
    }

    override fun deleteBank(accountNumber: String) {
        val bankToBeRemoved = banks.firstOrNull { bank -> bank.accountNumber == accountNumber }
            ?: throw UnprocessableEntityException("The bank with account number $accountNumber not exists.")

        banks.remove(bankToBeRemoved)
    }

}