package com.example.data

import com.example.model.Movie
import com.example.model.MovieDifficulty
import com.example.model.MovieLanguage

object SouthIndianMovies {
    val list: List<Movie> = listOf(
        // Pan-India, Telugu, Tamil, Kannada, Malayalam Blockbusters
        Movie("south_rrr", "RRR", "రౌద్రం రణం రుధిరం", 2022, MovieLanguage.SOUTH_INDIAN, "Epic Period Action", MovieDifficulty.EASY, "", "N.T. Rama Rao Jr., Ram Charan, Alia Bhatt, Ajay Devgn"),
        Movie("south_baahubali1", "Baahubali: The Beginning", "బాహుబలి", 2015, MovieLanguage.SOUTH_INDIAN, "Epic Fantasy Action", MovieDifficulty.EASY, "", "Prabhas, Rana Daggubati, Anushka Shetty, Tamannaah"),
        Movie("south_baahubali2", "Baahubali 2: The Conclusion", "బాహుబలి २", 2017, MovieLanguage.SOUTH_INDIAN, "Epic Fantasy Action", MovieDifficulty.EASY, "", "Prabhas, Rana Daggubati, Anushka Shetty, Ramya Krishna"),
        Movie("south_kalki", "Kalki 2898 AD", "కల్కి 2898 AD", 2024, MovieLanguage.SOUTH_INDIAN, "Epic Dystopian Sci-Fi", MovieDifficulty.EASY, "", "Prabhas, Amitabh Bachchan, Kamal Haasan, Deepika Padukone"),
        Movie("south_salaar", "Salaar: Part 1 – Ceasefire", "సలార్", 2023, MovieLanguage.SOUTH_INDIAN, "Neo-Noir Action Thriller", MovieDifficulty.EASY, "", "Prabhas, Prithviraj Sukumaran, Shruti Haasan"),
        Movie("south_pushpa", "Pushpa: The Rise", "పుష్ప", 2021, MovieLanguage.SOUTH_INDIAN, "Action Drama", MovieDifficulty.EASY, "", "Allu Arjun, Rashmika Mandanna, Fahadh Faasil"),
        Movie("south_pushpa2", "Pushpa 2: The Rule", "పుష్ప २", 2024, MovieLanguage.SOUTH_INDIAN, "Action Drama Blockbuster", MovieDifficulty.EASY, "", "Allu Arjun, Rashmika Mandanna, Fahadh Faasil"),
        Movie("south_ala_vaikunthapurramuloo", "Ala Vaikunthapurramuloo", "అల వైకుంఠపురములో", 2020, MovieLanguage.SOUTH_INDIAN, "Action Comedy Drama", MovieDifficulty.EASY, "", "Allu Arjun, Pooja Hegde, Tabu, Jayaram"),
        Movie("south_sarileru", "Sarileru Neekevvaru", "సరిలేరు నీకెవ్వరు", 2020, MovieLanguage.SOUTH_INDIAN, "Action Comedy", MovieDifficulty.MEDIUM, "", "Mahesh Babu, Rashmika Mandanna, Vijayashanti"),
        Movie("south_srimanthudu", "Srimanthudu", "శ్రీమంతుడు", 2015, MovieLanguage.SOUTH_INDIAN, "Action Drama", MovieDifficulty.MEDIUM, "", "Mahesh Babu, Shruti Haasan, Jagapathi Babu"),
        Movie("south_pokiri", "Pokiri", "పోకిరి", 2006, MovieLanguage.SOUTH_INDIAN, "Action Thriller", MovieDifficulty.EASY, "", "Mahesh Babu, Ileana D'Cruz, Prakash Raj"),
        Movie("south_rangasthalam", "Rangasthalam", "రంగస్థలం", 2018, MovieLanguage.SOUTH_INDIAN, "Period Action Drama", MovieDifficulty.MEDIUM, "", "Ram Charan, Samantha Ruth Prabhu, Aadhi Pinisetty"),
        Movie("south_magadheera", "Magadheera", "మగధీర", 2009, MovieLanguage.SOUTH_INDIAN, "Reincarnation Fantasy Action", MovieDifficulty.EASY, "", "Ram Charan, Kajal Aggarwal, Dev Gill, Srihari"),
        Movie("south_eega", "Eega (Makkhi)", "ఈగ / मक्खी", 2012, MovieLanguage.SOUTH_INDIAN, "Fantasy Revenge", MovieDifficulty.EASY, "", "Nani, Samantha, Sudeep"),
        Movie("south_jersey", "Jersey", "జెర్సీ", 2019, MovieLanguage.SOUTH_INDIAN, "Sports Drama", MovieDifficulty.MEDIUM, "", "Nani, Shraddha Srinath, Sathyaraj"),
        Movie("south_hi_nanna", "Hi Nanna", "హాయ్ నాన్న", 2023, MovieLanguage.SOUTH_INDIAN, "Romantic Family Drama", MovieDifficulty.MEDIUM, "", "Nani, Mrunal Thakur, Baby Kiara"),
        Movie("south_dasara", "Dasara", "దసరా", 2023, MovieLanguage.SOUTH_INDIAN, "Period Action Drama", MovieDifficulty.MEDIUM, "", "Nani, Keerthy Suresh, Dheekshith Shetty"),
        Movie("south_sita_ramam", "Sita Ramam", "సీతా రామం", 2022, MovieLanguage.SOUTH_INDIAN, "Period Romantic Drama", MovieDifficulty.EASY, "", "Dulquer Salmaan, Mrunal Thakur, Rashmika Mandanna"),
        Movie("south_hanuman", "Hanu-Man", "హను-మాన్", 2024, MovieLanguage.SOUTH_INDIAN, "Superhero Fantasy", MovieDifficulty.EASY, "", "Teja Sajja, Amritha Aiyer, Varalaxmi Sarathkumar"),
        Movie("south_arjun_reddy", "Arjun Reddy", "అర్జున్ రెడ్డి", 2017, MovieLanguage.SOUTH_INDIAN, "Romantic Drama", MovieDifficulty.EASY, "", "Vijay Deverakonda, Shalini Pandey, Rahul Ramakrishna"),
        Movie("south_geetha_govindam", "Geetha Govindam", "గీత గోవిందం", 2018, MovieLanguage.SOUTH_INDIAN, "Romantic Comedy", MovieDifficulty.EASY, "", "Vijay Deverakonda, Rashmika Mandanna, Subbaraju"),
        // Tamil Cinema
        Movie("south_vikram", "Vikram", "விக்ரம்", 2022, MovieLanguage.SOUTH_INDIAN, "Action Thriller (LCU)", MovieDifficulty.EASY, "", "Kamal Haasan, Vijay Sethupathi, Fahadh Faasil, Suriya"),
        Movie("south_jailer", "Jailer", "ஜெயிலர்", 2023, MovieLanguage.SOUTH_INDIAN, "Action Comedy / Drama", MovieDifficulty.EASY, "", "Rajinikanth, Vinayakan, Ramya Krishnan, Mohanlal, Shiva"),
        Movie("south_leo", "Leo", "லியோ", 2023, MovieLanguage.SOUTH_INDIAN, "Action Thriller (LCU)", MovieDifficulty.EASY, "", "Thalapathy Vijay, Sanjay Dutt, Trisha, Arjun Sarja"),
        Movie("south_master", "Master", "மாஸ்டர்", 2021, MovieLanguage.SOUTH_INDIAN, "Action Thriller", MovieDifficulty.EASY, "", "Thalapathy Vijay, Vijay Sethupathi, Malavika Mohanan"),
        Movie("south_mersal", "Mersal", "மெர்சல்", 2017, MovieLanguage.SOUTH_INDIAN, "Action Thriller", MovieDifficulty.EASY, "", "Thalapathy Vijay, S.J. Suryah, Samantha, Kajal, Nithya"),
        Movie("south_thuppakki", "Thuppakki", "துப்பாக்கி", 2012, MovieLanguage.SOUTH_INDIAN, "Action Thriller", MovieDifficulty.EASY, "", "Thalapathy Vijay, Kajal Aggarwal, Vidyut Jammwal"),
        Movie("south_ghilli", "Ghilli", "கில்லி", 2004, MovieLanguage.SOUTH_INDIAN, "Sports Action Romance", MovieDifficulty.EASY, "", "Thalapathy Vijay, Trisha Krishnan, Prakash Raj"),
        Movie("south_ponniyin_selvan_1", "Ponniyin Selvan: I", "பொன்னியின் செல்வன் १", 2022, MovieLanguage.SOUTH_INDIAN, "Epic Historical Action", MovieDifficulty.MEDIUM, "", "Vikram, Aishwarya Rai, Jayam Ravi, Karthi, Trisha"),
        Movie("south_ponniyin_selvan_2", "Ponniyin Selvan: II", "பொன்னியின் செல்வன் २", 2023, MovieLanguage.SOUTH_INDIAN, "Epic Historical Action", MovieDifficulty.MEDIUM, "", "Vikram, Aishwarya Rai, Jayam Ravi, Karthi, Trisha"),
        Movie("south_kaithi", "Kaithi", "கைதி", 2019, MovieLanguage.SOUTH_INDIAN, "Action Thriller", MovieDifficulty.EASY, "", "Karthi, Narain, Arjun Das, Harish Uthaman"),
        Movie("south_jai_bhim", "Jai Bhim", "ஜெய் பீம்", 2021, MovieLanguage.SOUTH_INDIAN, "Legal Drama", MovieDifficulty.MEDIUM, "", "Suriya, Lijomol Jose, Manikandan, Rajisha Vijayan"),
        Movie("south_soorarai_pottru", "Soorarai Pottru", "சூரரைப் போற்று", 2020, MovieLanguage.SOUTH_INDIAN, "Biographical Drama", MovieDifficulty.MEDIUM, "", "Suriya, Aparna Balamurali, Paresh Rawal"),
        Movie("south_vikram_vedha", "Vikram Vedha", "விக்ரம் வேதா", 2017, MovieLanguage.SOUTH_INDIAN, "Neo-Noir Action Thriller", MovieDifficulty.EASY, "", "Madhavan, Vijay Sethupathi, Shraddha Srinath, Varalaxmi"),
        Movie("south_super_deluxe", "Super Deluxe", "சூப்பர் டீலக்ஸ்", 2019, MovieLanguage.SOUTH_INDIAN, "Hyperlink Dark Comedy", MovieDifficulty.HARD, "", "Vijay Sethupathi, Fahadh Faasil, Samantha, Ramya"),
        Movie("south_96", "96", "९६", 2018, MovieLanguage.SOUTH_INDIAN, "Romantic Drama", MovieDifficulty.MEDIUM, "", "Vijay Sethupathi, Trisha Krishnan, Devadarshini"),
        Movie("south_asuran", "Asuran", "அசுரன்", 2019, MovieLanguage.SOUTH_INDIAN, "Period Action Drama", MovieDifficulty.MEDIUM, "", "Dhanush, Manju Warrier, Prakash Raj, Pasupathy"),
        Movie("south_karnan", "Karnan", "கர்ணன்", 2021, MovieLanguage.SOUTH_INDIAN, "Action Drama", MovieDifficulty.HARD, "", "Dhanush, Lal, Rajisha Vijayan, Yogi Babu"),
        Movie("south_enthiran", "Enthiran (Robot)", "எந்திரன்", 2010, MovieLanguage.SOUTH_INDIAN, "Sci-Fi Action", MovieDifficulty.EASY, "", "Rajinikanth, Aishwarya Rai, Danny Denzongpa"),
        Movie("south_2_point_0", "2.0", "२.०", 2018, MovieLanguage.SOUTH_INDIAN, "Sci-Fi Action", MovieDifficulty.EASY, "", "Rajinikanth, Akshay Kumar, Amy Jackson"),
        Movie("south_sivaji", "Sivaji: The Boss", "சிவாஜி", 2007, MovieLanguage.SOUTH_INDIAN, "Masala Action", MovieDifficulty.EASY, "", "Rajinikanth, Shriya Saran, Suman, Vivek"),
        Movie("south_petta", "Petta", "பேட்ட", 2019, MovieLanguage.SOUTH_INDIAN, "Action Drama", MovieDifficulty.EASY, "", "Rajinikanth, Vijay Sethupathi, Simran, Trisha, Nawazuddin"),
        Movie("south_anniyan", "Anniyan (Aparichit)", "அந்நியன்", 2005, MovieLanguage.SOUTH_INDIAN, "Psychological Action Thriller", MovieDifficulty.EASY, "", "Vikram, Sadha, Prakash Raj, Vivek"),
        // Kannada Cinema
        Movie("south_kgf1", "K.G.F: Chapter 1", "ಕೆ.ಜಿ.ಎಫ್: ಅಧ್ಯಾಯ ೧", 2018, MovieLanguage.SOUTH_INDIAN, "Period Action Thriller", MovieDifficulty.EASY, "", "Yash, Srinidhi Shetty, Ramachandra Raju, Anant Nag"),
        Movie("south_kgf2", "K.G.F: Chapter 2", "ಕೆ.ಜಿ.ಎಫ್: ಅಧ್ಯಾಯ ೨", 2022, MovieLanguage.SOUTH_INDIAN, "Period Action Thriller", MovieDifficulty.EASY, "", "Yash, Sanjay Dutt, Raveena Tandon, Srinidhi Shetty"),
        Movie("south_kantara", "Kantara", "ಕಾಂತಾರ", 2022, MovieLanguage.SOUTH_INDIAN, "Deva Folklore Action Thriller", MovieDifficulty.EASY, "", "Rishab Shetty, Sapthami Gowda, Kishore, Achyuth Kumar"),
        Movie("south_777_charlie", "777 Charlie", "೭೭೭ ಚಾರ್ಲಿ", 2022, MovieLanguage.SOUTH_INDIAN, "Adventure Comedy Drama", MovieDifficulty.EASY, "", "Rakshit Shetty, Sangeetha Sringeri, Raj B. Shetty"),
        Movie("south_kirik_party", "Kirik Party", "ಕಿರಿಕ್ ಪಾರ್ಟಿ", 2016, MovieLanguage.SOUTH_INDIAN, "Campus Comedy Drama", MovieDifficulty.MEDIUM, "", "Rakshit Shetty, Rashmika Mandanna, Samyuktha Hegde"),
        Movie("south_garuda_gamana", "Garuda Gamana Vrishabha Vahana", "ಗರುಡ ಗಮನ ವೃಷಭ ವಾಹನ", 2021, MovieLanguage.SOUTH_INDIAN, "Neo-Noir Gangster Drama", MovieDifficulty.HARD, "", "Raj B. Shetty, Rishab Shetty, Gopalkrishna Deshpande"),
        Movie("south_lucia", "Lucia", "ಲೂಸಿಯಾ", 2013, MovieLanguage.SOUTH_INDIAN, "Psychological Thriller", MovieDifficulty.HARD, "", "Sathish Ninasam, Sruthi Hariharan, Achyuth Kumar"),
        Movie("south_vikrant_rona", "Vikrant Rona", "ವಿಕ್ರಾಂತ್ ರೋಣ", 2022, MovieLanguage.SOUTH_INDIAN, "Fantasy Action Thriller", MovieDifficulty.MEDIUM, "", "Kichcha Sudeepa, Nirup Bhandari, Neetha Ashok"),
        // Malayalam Cinema
        Movie("south_manjummel_boys", "Manjummel Boys", "മഞ്ഞുമ്മൽ ബോയ്സ്", 2024, MovieLanguage.SOUTH_INDIAN, "Survival Thriller", MovieDifficulty.EASY, "", "Soubin Shahir, Sreenath Bhasi, Balu Varghese"),
        Movie("south_aavesham", "Aavesham", "ആവേശം", 2024, MovieLanguage.SOUTH_INDIAN, "Action Comedy", MovieDifficulty.EASY, "", "Fahadh Faasil, Hipzster, Mithun Jai Shankar, Roshan"),
        Movie("south_the_goat_life", "The Goat Life (Aadujeevitham)", "ആടുജീവിതം", 2024, MovieLanguage.SOUTH_INDIAN, "Survival Drama", MovieDifficulty.MEDIUM, "", "Prithviraj Sukumaran, Amala Paul, Jimmy Jean-Louis"),
        Movie("south_bramayugam", "Bramayugam", "ഭ്രമയുഗം", 2024, MovieLanguage.SOUTH_INDIAN, "Folk Horror Mystery", MovieDifficulty.HARD, "", "Mammootty, Arjun Ashokan, Sidharth Bharathan"),
        Movie("south_drishyam_malayalam", "Drishyam", "ദൃശ്യം", 2013, MovieLanguage.SOUTH_INDIAN, "Crime Thriller", MovieDifficulty.EASY, "", "Mohanlal, Meena, Ansiba Hassan, Asha Sharath"),
        Movie("south_drishyam2_malayalam", "Drishyam 2", "ദൃശ്യം २", 2021, MovieLanguage.SOUTH_INDIAN, "Crime Thriller", MovieDifficulty.EASY, "", "Mohanlal, Meena, Ansiba Hassan, Murali Gopy"),
        Movie("south_lucifer", "Lucifer", "ലൂസിഫർ", 2019, MovieLanguage.SOUTH_INDIAN, "Political Action Thriller", MovieDifficulty.EASY, "", "Mohanlal, Vivek Oberoi, Manju Warrier, Tovino"),
        Movie("south_premam", "Premam", "പ്രേമം", 2015, MovieLanguage.SOUTH_INDIAN, "Coming-of-Age Romantic Comedy", MovieDifficulty.EASY, "", "Nivin Pauly, Sai Pallavi, Madonna Sebastian, Anupama"),
        Movie("south_bangalore_days", "Bangalore Days", "ബാംഗ്ലൂർ ഡേയ്സ്", 2014, MovieLanguage.SOUTH_INDIAN, "Coming-of-Age Drama", MovieDifficulty.EASY, "", "Dulquer Salmaan, Nivin Pauly, Nazriya Nazim, Fahadh"),
        Movie("south_kumbalangi_nights", "Kumbalangi Nights", "കുമ്പളങ്ങി നൈറ്റ്സ്", 2019, MovieLanguage.SOUTH_INDIAN, "Family Comedy Drama", MovieDifficulty.MEDIUM, "", "Shane Nigam, Soubin Shahir, Fahadh Faasil, Anna Ben"),
        Movie("south_minnal_murali", "Minnal Murali", "മിന്നൽ മുരളി", 2021, MovieLanguage.SOUTH_INDIAN, "Superhero Action Comedy", MovieDifficulty.EASY, "", "Tovino Thomas, Guru Somasundaram, Aju Varghese"),
        Movie("south_2018_movie", "2018: Everyone is a Hero", "२०१८", 2023, MovieLanguage.SOUTH_INDIAN, "Disaster Survival Drama", MovieDifficulty.EASY, "", "Tovino Thomas, Kunchacko Boban, Asif Ali, Vineeth"),
        Movie("south_romancham", "Romancham", "രോമാഞ്ചം", 2023, MovieLanguage.SOUTH_INDIAN, "Horror Comedy", MovieDifficulty.MEDIUM, "", "Soubin Shahir, Arjun Ashokan, Sajin Gopu"),
        Movie("south_hridayam", "Hridayam", "ഹൃദയം", 2022, MovieLanguage.SOUTH_INDIAN, "Coming-of-age Musical Romance", MovieDifficulty.MEDIUM, "", "Pranav Mohanlal, Kalyani Priyadarshan, Darshana")
    )
}
