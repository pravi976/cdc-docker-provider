package com.fedex.cdc.demo.provider;

import com.fedex.cdc.provider.AbstractCdcLocalPactProviderVerificationTest;
import com.fedex.cdc.provider.CdcPactProviderVerification;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;

@CdcPactProviderVerification(provider = "supply-inventory-provider")
@EnabledIfEnvironmentVariable(named = "PACT_FOLDER", matches = ".+")
class InventoryProviderLocalPactVerificationTest extends AbstractCdcLocalPactProviderVerificationTest {
}

