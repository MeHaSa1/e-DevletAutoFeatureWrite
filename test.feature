Feature: Arsa Metrekare Birim Değeri Sorgulama
	Scenario Outline: Bu hizmeti kullanarak belediye sınırları içerisindeki cadde ve sokaklara ait arsa rayiç değerini (metrekare birim değeri) yıllara göre sorgulayabilirsiniz.
		Given user is on the page
		When user enters "<value1>" to *Mahalle Adı
		And user enters "<value2>" to Cadde/Sokak Adı
		And user enters "<value3>" to *Yıl
		And user presses button
		Then user should be directed to next page
		Examples:
			| value1 | value2 | value3 | 
