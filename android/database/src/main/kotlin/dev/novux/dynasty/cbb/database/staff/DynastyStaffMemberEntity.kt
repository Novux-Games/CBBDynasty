package dev.novux.dynasty.cbb.database.staff

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "dynasty_staff_member",
    indices = [Index(value = ["dynastyId"])],
)
data class DynastyStaffMemberEntity(
    @PrimaryKey val staffId: String,
    val dynastyId: String,
    val staffKindWire: String,
    val roleWire: String,
    val firstName: String,
    val lastName: String,
    val age: Int = 35,
    val recruitingRating: Int = 0,
    val developmentRating: Int = 0,
    val scoutingRating: Int = 0,
    val supportContributionRating: Int = 0,
    val yearsInRole: Int = 1,
    val yearsAtSchool: Int = 1,
    val contractYearsLeft: Int = 2,
    val contractLengthYears: Int = 3,
)
