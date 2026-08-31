## Creating a multi-branch pipeline

In most cases we need pipelines for every branch and configure the build for each. For example:
- Main branch > Test, build and deploy
- Feature/bug-fix branch > Test, build

### Creating on Jenkins

- On Jenkins > New Item > Multibranch Pipeline 
- Add Git repository URL > Discover branches > Filter by name (with regular expressions)
- Jenkins will automatically build and detect branches that have a Jenkinsfile
- Add `^multibranch-.*` if you want to limit build to branches starting with *multibranch-*

### Branch based logic

 - To run tests on all branches but only build and deploy the app on *multibranch-pipeline*, checkout a separate branch *multibranch-test* and add build conditions to your Jenkinsfile. See [commit](https://github.com/JonathanBaqDev/TWN-Jenkins/commit/960394b4b0c8b9db0f5b381b995ac4b3207e3454)

 - On Jenkins, click "Scan multibranch pipeline now"