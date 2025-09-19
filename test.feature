Feature: 
	Scenario: Bu hizmeti kullanarak, Öğrenci bilgilerinizi görüntüleyebilir ve barkodlu Öğrenci belgenizi oluşturabilirsiniz.
		Given user is on the page "https://www.turkiye.gov.tr/yok-ogrenci-belgesi-sorgulama"
		When user clicks the Belge Oluştur link
		Then user should be directed to next page
