## Creating a multi-branch pipeline

In most cases we need pipelines for every branch and configure the build for each. For example:
- Main branch > Test, build and deploy
- Feature/bug-fix branch > Test, build

### Creating on Jenkins

- On Jenkins > New Item > Multibranch Pipeline 
- Add Git repository URL > Discover branches > Filter by name (with regular expressions)
- Jenkins will automatically build and detect branches that have a Jenkinsfile
- Add `^multibranch-.*` if you want to limit builds to this current branch first
