def call() {
    timeout(time: 5, unit: 'MINUTES') {
        def reportTask = readFile('.scannerwork/report-task.txt')
        def ceTaskUrl = ''
        reportTask.splitEachLine('=') { fields ->
            if (fields[0] == 'ceTaskUrl') {
                ceTaskUrl = fields[1..-1].join('=')
            }
        }
        
        echo "Polling SonarQube task URL: ${ceTaskUrl}"
        
        def taskStatus = 'PENDING'
        while (taskStatus == 'PENDING' || taskStatus == 'IN_PROGRESS') {
            sleep(10)
            def response = sh(script: "curl -s ${ceTaskUrl}", returnStdout: true).trim()
            
            if (response.contains('"status":"SUCCESS"')) {
                taskStatus = 'SUCCESS'
            } else if (response.contains('"status":"FAILED"')) {
                error("SonarQube analysis task failed on server.")
            } else if (response.contains('"status":"CANCELED"')) {
                error("SonarQube analysis task was canceled.")
            } else {
                echo "SonarQube task status is still ${taskStatus}... waiting."
            }
        }
        
        def qgResponse = sh(script: "curl -s 'http://54.190.13.96:9000/api/qualitygates/project_status?projectKey=mega'", returnStdout: true).trim()
        echo "Quality Gate Response: ${qgResponse}"
        
        if (!qgResponse.contains('"status":"OK"')) {
            error("Pipeline aborted due to Quality Gate failure (Status is not OK).")
        } else {
            echo "SonarQube Quality Gate passed successfully!"
        }
    }
}