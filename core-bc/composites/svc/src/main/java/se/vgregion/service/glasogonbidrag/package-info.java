/**
 * @author Martin Lind - Monator Technologies AB
 */
package se.vgregion.service.glasogonbidrag;

/*
 * TODO: Consider move to a better package structure.
 *
 * two top-level packages
 *   - se.vgregion.service.glasogonbidrag.service
 *   - se.vgregion.service.glasogonbidrag.types
 *
 * In services have domain, integration and local.
 * Same in types.
 *
 * Sort current types, these should be part of domain.
 *  - BeneficiaryAreaTuple
 *  - BeneficiaryGrantTuple
 *  - BeneficiaryIdentificationTuple
 *  - BeneficiaryNameTuple
 *  - InvoiceBeneficiaryIdentificationTuple
 *  - InvoiceBeneficiaryTuple
 *  - InvoiceGrantTuple
 *  - LowLevelSortOrder
 *  - OrderType
 *
 * This is part of Integration:
 *  - BeneficiaryTransport
 *
 * This is part of local (validation of grant rules):
 *  - GrantRuleEntry
 *  - GrantRuleResult
 *  - GrantRuleViolation
 *  - GrantRuleWarning
 *
 * This is part of local (statistic report)
 *  - StatisticSearchDateInterval
 *  - StatisticSearchRequest
 *  - StatisticSearchResponse
 *  - StatisticSearchType
 *
 */
