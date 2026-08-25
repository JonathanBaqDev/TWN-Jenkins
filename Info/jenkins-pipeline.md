## From freestyle project to pipeline

In this section we will create a pipeline in Jenkins. While freestyle jobs are simpler to configure through the Jenkins UI, they are harder to reproduce, share, and maintain as projects grow.

A pipeline defines the entire build process as code in a Jenkinsfile. This makes it version-controlled, repeatable, easier to review, and capable of handling complex stages, approvals, parallel tasks, and deployment workflows.

### Create a pipeline

- In Jenkins, create a pipeline
- Under Pipeline > select script from source code management (SCM)
- Select Git > add the project repository, branch as well as repo credentials and save (see [*jenkins-docker*](https://github.com/JonathanBaqDev/TWN-Jenkins/blob/jenkins-docker/Info/jenkins-docker.md) for more info on how to add credentials in Jenkins)
- Create a Jenkinsfile (see [commit](https://github.com/JonathanBaqDev/TWN-Jenkins/commit/a0c0d7b9eb4699ce3b2b33e0eba0a16108f0d53b))
- Build the pipeline, you can see the stages defined in the Jenkins file and the logs for each

### Jenkinsfile

- The Jenkinsfile can be written in two ways:
    - Scripted (using the programming language Groovy) - this is more flexible and better for complex builds 
    - Declarative - easier to start with and what we will opt for

#### Build Conditions

- A 'post' build step can be defined with appropriate run conditions as needed (run on 'success', 'failure', 'always', etc)
- Conditionals can be defined for each stage using 'when' and 'expression'

#### Environment variables

- Some environment variables like 'BRANCH_NAME' is avaialble by default, these can be found in '<server_ip>:<jenkins_port>/**env-vars.html**'
- Custom environment variables can be defined in the Jenkinsfile using the 'environment' directive

#### Using credentials

- On Jenkins, the Credentials Binding plugin can be added to allow binding credentials as variables. It can be defined in the Jenkinsfile under 'environment' like so `CREDENTIAL_VARIABLE = credentials('<credential_id>')`
- If credentials only need to be used in a specific step, *wrappers* can be used under the specific 'steps' like so
```
withCredentials([
    usernamePassword(credentials: '<credential_id>', usernameVariable: USER, passwordVariable: PWD)
]) {
    sh "some script ${USER} ${PWD}"
}
```

#### Build Tools

- Can be defined in the 'tools' directive and supports gradle, maven and jdk. Other tools like npm or yarn can be installed separately, check *Build tools* in the [README](https://github.com/JonathanBaqDev/TWN-Jenkins/blob/jenkins-pipeline/README.md) for more info
- When defining tools, you must use the exact name configured in Jenkins > Tools
```
tools {
    maven "<configured_tool_name>"
}
```

#### Parameters

- You can define parameters to use in expressions / conditions
```
parameters {
    choice(name: version, choices:['1.1.0', '2.2.0', '1.3.0'])
    booleanParam(name:'executeTests', defaultValue: true, description:'')
}
...
    stage("test"){
        when {
            expression {
                params.executeTests == true
            }
        }
    }
    stage("deploy") {
        steps {
            echo "deploying version ${params.version}"
        }
    }
```
- The 'booleanParam' will show a checkbox and 'choices' will show a dropdown when you build your pipeline with parameters on Jenkins

#### Groovy scripts

- Groovy scripts can be defined within 'steps' using the 'script' tag
- Script files can be loaded inside the 'script'
```
def gv
...
    script {
        gv = load "<file_name>.groovy"
        gv.<functionName>()
    }
```
- All environment variables in Jenkinsfile are available in the Groovy script

#### User Input

- In the 'stage' section
```
stage("deploy") {
    input {
        message "Select environment to deploy to"
        ok "Done"
        parameters {
            choice(name: 'ENV', choices:['dev', 'staging', 'prod'], description:'')
        }
    }
    steps {
        script{
            echo "Deploying to ${ENV}"
        }
    }
}
```
- In the 'script' section
```
stage("deploy") {
    steps {
        script{
            // Example of direct assignment to env variable
            env.ENV = input message: "Select environment to deploy to", ok: "Done", parameters: [choice(name: 'ENV', choices:['dev', 'staging', 'prod'], description:'')]
            echo "Deploying to ${ENV}"
        }
    }
}
```