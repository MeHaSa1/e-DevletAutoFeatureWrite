Feature: Askıdaki İmar Planı Sorgulama
	Scenario: Bu hizmeti kullanarak belediye sınırları içerisinde yer alan askıya alınmış imar planlarını sorgulayabilirsiniz.
		Given user is on the page "https://www.turkiye.gov.tr/onikisubat-belediyesi-askidaki-imar-plani-sorgulama"
		Then user should see table Askıdaki İmar Planı Listesi with columns:Askıya Çıkış Tarihi/Askıdan İniş Tarihi/Açıklama/İlçe/Mahalle/İşlem/
		When user clicks the Yazdır link
		Then user should be directed to next page
