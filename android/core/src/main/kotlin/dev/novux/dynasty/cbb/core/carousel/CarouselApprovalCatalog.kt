package dev.novux.dynasty.cbb.core.carousel

object CarouselApprovalCatalog {
    fun card(id: String, kind: CarouselApprovalKind): CarouselApprovalCard =
        when (kind) {
            CarouselApprovalKind.ContractExtension -> CarouselApprovalCard(
                id = id,
                kind = kind,
                headline = "Athletic director offered a contract extension",
                detail = "Review years, salary, and goals before offseason Week 1.",
            )
            CarouselApprovalKind.HeadCoachOffer -> CarouselApprovalCard(
                id = id,
                kind = kind,
                headline = "Head coaching offer",
                detail = "Accept, decline, or negotiate before other schools fill the role.",
            )
            CarouselApprovalKind.AssistantHire -> CarouselApprovalCard(
                id = id,
                kind = kind,
                headline = "Assistant coach hire",
                detail = "Approve salary and role for your staff.",
            )
        }
}
