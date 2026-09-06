## Automatic builds with Webhooks

We will configure Jenkins and Github to build pipelines when commits are made to the repository

### On Jenkins

- Manage Jenkins > Plugins > Install build trigger plugin for your SCM (Gitlab, Github, etc)
- System > Find the section for Gitlab/Github and the connection name, URL, credentials
- Credentials will use tokens, the Github plugin will for example provide instructions how to configure your personal access token on Github as a credential in Jenkins
- On Jenkins pipeline > Configure > Build trigger 
- On a pipeline enable the buid trigger in Configure > Triggers 

### On Github

- On your repository > Settings > Webhooks
- Add a Webhook and configure with Jenkins URL, trigger (eg. on Push or Merge request), authentication. The process will be similar in Gitlab - this will be found in Account settings > Integrations
- Trigger a test connection or make a commit and push and confirm the build was triggered in Jenkins

### For Multibranch pipelines

- On Jenkins > Plugins > Install the Multibranch Scan Webhook Trigger Version plugin
- On the multibranch pipeline > Configure > Scan Multibranch Pipeline Triggers > Scan by webhook can now be enabled
- Configure trigger token
- In Github/Gitlab > Settings > Webhooks - add the URL where Jenkins will be listening for requests: `JENKINS_URL/multibranch-webhook-trigger/invoke?token=[Trigger token]`
