def call(){
  dependencyCheck additionalArguments: '--scan ./ --nvdApiKey 365C631A-A0A2-4D3A-BB13-9BF29BE3F7A1', odcInstallation: 'OWASP'
  dependencyCheckPublisher pattern: '**/dependency-check-report.xml'
}