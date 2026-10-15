package com.example.data

import com.example.model.AppLanguage

data class GangPair(
    val team1Nepali: String,
    val team1English: String,
    val team1Emoji: String,
    val team2Nepali: String,
    val team2English: String,
    val team2Emoji: String
) {
    fun getTeam1Name(language: AppLanguage): String =
        if (language == AppLanguage.NEPALI) team1Nepali else team1English

    fun getTeam2Name(language: AppLanguage): String =
        if (language == AppLanguage.NEPALI) team2Nepali else team2English
}

data class SingleGang(
    val nepaliName: String,
    val englishName: String,
    val emoji: String
) {
    fun getName(language: AppLanguage): String =
        if (language == AppLanguage.NEPALI) nepaliName else englishName
}

object NepaliGangNames {
    val PAIRS: List<GangPair> = listOf(
        GangPair("सिङ्गल ग्याङ", "Single Gang", "💔", "मिङ्गल ग्याङ", "Mingle Gang", "💑"),
        GangPair("बालेन ग्याङ", "Balen Gang", "🕶️", "रवि ग्याङ", "Ravi Gang", "🎤"),
        GangPair("ओली ग्याङ", "Oli Gang", "🪈", "देउवा ग्याङ", "Deuba Gang", "🏛️"),
        GangPair("प्रचण्ड ग्याङ", "Prachanda Gang", "🚩", "बाबुराम ग्याङ", "Baburam Gang", "🎓"),
        GangPair("मुकुन्दे ग्याङ", "Mukunde Gang", "📢", "हर्क ग्याङ", "Harka Gang", "⛏️"),
        GangPair("कुलमान ग्याङ", "Kulman Gang", "💡", "धिरेन ग्याङ", "Dhiren Gang", "⚡"),
        GangPair("राजेश हमाल ग्याङ", "Rajesh Hamal Gang", "🦁", "बिराज भट्ट ग्याङ", "Biraj Bhatta Gang", "🥋"),
        GangPair("ज्योति मगर ग्याङ", "Jyoti Magar Gang", "💃", "प्रिती आले ग्याङ", "Priti Ale Gang", "🎤"),
        GangPair("सिधा कुरा ग्याङ", "Sidha Kura Gang", "📺", "घटना र विचार ग्याङ", "Ghatna Ra Vichar Gang", "📰"),
        GangPair("गफाडी ग्याङ", "Gafadi Gang", "🗣️", "कामचोर ग्याङ", "Kamchor Gang", "😴"),
        GangPair("अलछिना ग्याङ", "Alachhina Gang", "🤦", "अलछिने ग्याङ", "Alachhine Gang", "🤷"),
        GangPair("झ्याप्प ग्याङ", "Jhyappa Gang", "🍻", "टल्ली ग्याङ", "Tally Gang", "🍾"),
        GangPair("ढिलो ग्याङ", "Dhilo Gang", "🐢", "बिर्से ग्याङ", "Birse Gang", "🧠"),
        GangPair("फेसबुक ग्याङ", "Facebook Gang", "📱", "टिकटक ग्याङ", "TikTok Gang", "🤳"),
        GangPair("मःमः ग्याङ", "MoMo Gang", "🥟", "चाउमिन ग्याङ", "Chowmein Gang", "🍜"),
        GangPair("सोझा ग्याङ", "Sojha Gang", "😇", "बाठा ग्याङ", "Batha Gang", "🦊"),
        GangPair("टोपी ग्याङ", "Topi Gang", "🧢", "चस्मा ग्याङ", "Chasma Gang", "👓"),
        GangPair("फुर्के ग्याङ", "Phurke Gang", "🦚", "घमन्डी ग्याङ", "Ghamandi Gang", "🦁"),
        GangPair("लोभी ग्याङ", "Lobi Gang", "💰", "दानी ग्याङ", "Dani Gang", "🎁"),
        GangPair("अल्छी ग्याङ", "Alchi Gang", "🦥", "फूर्तिलो ग्याङ", "Phurtilo Gang", "🐆"),
        GangPair("मायालु ग्याङ", "Mayalu Gang", "💕", "मोहिनी ग्याङ", "Mohini Gang", "✨"),
        GangPair("सोल्टी ग्याङ", "Solti Gang", "🕺", "सोल्टिनी ग्याङ", "Soltini Gang", "💃"),
        GangPair("भाले ग्याङ", "Bhale Gang", "🐓", "पोथी ग्याङ", "Pothi Gang", "🐥"),
        GangPair("लाहुरी ग्याङ", "Lahure Gang", "🎖️", "लाहुरिनी ग्याङ", "Lahureni Gang", "🌺"),
        GangPair("झाँक्री ग्याङ", "Jhakri Gang", "🥁", "धामी ग्याङ", "Dhami Gang", "🪘"),
        GangPair("काठे ग्याङ", "Kathe Gang", "🏞️", "पाखे ग्याङ", "Pakhe Gang", "🏔️"),
        GangPair("भाङ ग्याङ", "Bhang Gang", "🌿", "धतुरा ग्याङ", "Dhatura Gang", "🍃"),
        GangPair("आलु ग्याङ", "Aalu Gang", "🥔", "पिँडालु ग्याङ", "Pindalu Gang", "🍠"),
        GangPair("घिरौँला ग्याङ", "Ghiraula Gang", "🥒", "चिचिन्डा ग्याङ", "Chichinda Gang", "🥗"),
        GangPair("तितेकरेला ग्याङ", "Tite Karela Gang", "🥒", "चोचेभेनडी ग्याङ", "Choche Bhendi Gang", "🫑"),
        GangPair("फर्सी ग्याङ", "Pharsi Gang", "🎃", "इस्कुस ग्याङ", "Iskus Gang", "🍈"),
        GangPair("खानीमूला ग्याङ", "Khane Mula Gang", "🥕", "गाँजर ग्याङ", "Gajar Gang", "🥕")
    )

    fun getRandomPair(): GangPair {
        return PAIRS.random()
    }

    fun getRandomSingleGang(excludeName: String = ""): SingleGang {
        val allSingleGangs = PAIRS.flatMap {
            listOf(
                SingleGang(it.team1Nepali, it.team1English, it.team1Emoji),
                SingleGang(it.team2Nepali, it.team2English, it.team2Emoji)
            )
        }
        val candidates = allSingleGangs.filter { it.nepaliName != excludeName && it.englishName != excludeName }
        return if (candidates.isNotEmpty()) candidates.random() else allSingleGangs.random()
    }

    fun findPairForName(name: String): GangPair? {
        return PAIRS.find {
            it.team1Nepali.equals(name, ignoreCase = true) ||
            it.team1English.equals(name, ignoreCase = true) ||
            it.team2Nepali.equals(name, ignoreCase = true) ||
            it.team2English.equals(name, ignoreCase = true)
        }
    }

    fun translateName(name: String, targetLanguage: AppLanguage): String {
        for (pair in PAIRS) {
            if (pair.team1Nepali.equals(name, ignoreCase = true) || pair.team1English.equals(name, ignoreCase = true)) {
                return pair.getTeam1Name(targetLanguage)
            }
            if (pair.team2Nepali.equals(name, ignoreCase = true) || pair.team2English.equals(name, ignoreCase = true)) {
                return pair.getTeam2Name(targetLanguage)
            }
        }
        // Fallback or custom names
        if (targetLanguage == AppLanguage.NEPALI) {
            if (name == "Gang A" || name == "Team 1") return "ग्याङ १"
            if (name == "Gang B" || name == "Team 2") return "ग्याङ २"
        } else {
            if (name == "ग्याङ १" || name == "ग्याङ ए") return "Gang A"
            if (name == "ग्याङ २" || name == "ग्याङ बी") return "Gang B"
        }
        return name
    }
}
