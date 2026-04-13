package com.fedex.cdc.demo.provider;

import com.fedex.cdc.provider.AbstractCdcProviderVerificationTest;
import com.fedex.cdc.provider.CdcPactProviderVerification;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;

@CdcPactProviderVerification(provider = "supply-inventory-provider", publishResults = true)
@EnabledIfEnvironmentVariable(named = "PACT_BROKER_BASE_URL", matches = ".+")
class InventoryProviderPactVerificationTest extends AbstractCdcProviderVerificationTest {
}
