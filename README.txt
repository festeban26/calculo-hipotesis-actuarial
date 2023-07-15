Instructions on how to DEPLOY to Google Cloud Run

1. mvn clean install
2. OPEN Google Cloud SDK Shell
	2.1. Go to project directory
		cd C:\dev\projects\core-microservices\calculo-hipotesis-companyl
	2.2. Deploy to
		Production:
			gcloud run deploy calculo-hipotesis-companyl --source .
		Test:
			gcloud run deploy calculo-hipotesis-companyl-prueba --source .

GENERAL
gcloud config set project core-340817
gcloud config set run/region us-east1