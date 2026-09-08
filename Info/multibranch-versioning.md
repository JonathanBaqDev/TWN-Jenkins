# Incorporating automatic versioning to the build

App versions are best managed automatically by the pipeline build

## For Maven

 - We can use the [build-helper:parse-version](https://www.mojohaus.org/build-helper-maven-plugin/parse-version-mojo.html) tool

 - Example of incrementing the incremental/patch version (1.1.x) automatically

```
mvn build-helper:parse-version versions:set \
-DnewVersion=\${parsedVersion.majorVersion}.\${parsedVersion.minorVersion}.\${parsedVersion.nextIncrementalVersion} \
versions:commit
```
 - The concept will be the same with whatever package manager is used, eg. with npm or Gradle

 ## On the Jenkinsfile

 - Add the above command as a step before executing the `mvn package` step, see [commit](https://github.com/JonathanBaqDev/TWN-Jenkins/commit/f7bc596740cfd83a685ff83f393b1a72934f86a7)
 - Add versioning to the Docker image build as well, see [commit](https://github.com/JonathanBaqDev/TWN-Jenkins/commit/1eb6694f0b8d488add1f2ec024f7f03a6cb97d8b)
 - Clean the target folder before executing a build to make sure it will only have one .jar file when the pipeline is ran: `mvn clean package` instead of just `mvn package`
- Run the pipeline if you have not configured Webhooks and confirm the image build version

## Update app version on repo

- On Jenkins > Install the *Ignore Committer Strategy* plugin - this is used to prevent a *commit - build* loop on the pipeline with webhooks configured
- On the multibranch configuration > Build Sources > Build Strategies - Add the plugin
- Add the CI committer email to the ignore list - *jenkins@ci.com* if following the example linked below
- Add a step to commit the app build version on the pom.xml file back to the repo when a build is ran, see [commmit](https://github.com/JonathanBaqDev/TWN-Jenkins/commit/201ffb5869f7b7f8a1067f2c709fa505388a2fe5) and the changes to the [shared Jenkins library](https://github.com/JonathanBaqDev/TWN-Jenkins-Shared-Library/commit/4711800b7d609408228c34048931dca6f00e150a), if using it