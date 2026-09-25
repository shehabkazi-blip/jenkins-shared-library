def call(){
  withCredentials([string(credentialsId: 'NVD_API_KEY', variable: 'NVD_KEY')]) {
    dependencyCheck additionalArguments: "--scan ./ --nvdApiKey ${NVD_KEY} --autoUpdate false", odcInstallation: 'OWASP'
  }
  dependencyCheckPublisher pattern: '**/dependency-check-report.xml'
}