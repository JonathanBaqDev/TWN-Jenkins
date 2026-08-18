# Example of creating and configuring a freestyle job in Jenkins

### Create a freestyle job

- Configure > Build step > Select *Execute shell*
- Add `npm --version` in the commands, we installed Node and npm on the container directly so npm will be available
- Select Add build step > Invoke top-level Maven targets, Maven is configured as a plug in and this is how we can execute Maven commands
- Select the version you configured and add `--version` under Goals
- Save, click into the job and hit Build Now
- Click into the build and check Console Output

### Configure git repository

- Add a script file (see [commit](https://github.com/JonathanBaqDev/TWN-Jenkins/commit/569f7cdf8e19224c4fcce4e73a82aa54838aa784))
- Back on the freestyle job > Configure > Source code management
- Select Git and add your repository URL to this repo 
- Under Branches, add the branch you committed the script file to
- Under Shell Commands, replace with:
```
chmod +x <script_name>.sh
./<script_name>.sh
```
- Hit Add - Jenkins 
- On Jenkins Credentials Provider: fill out the username and password for your example repository.  
*If using Github, configure and use a personal access token instead of your account password. Check the official [Github Authentication Documentation](https://docs.github.com/en/authentication/keeping-your-account-and-data-secure/about-authentication-to-github#about-authentication-to-github) for more info*
- Fill out credential ID and hit Add
- Under Credentials - select your credential from the dropdown
- Hit Build Now and check the build console output
- You can check configured jobs in the Docker container in */var/jenkins_home/jobs*
- Fetched repositories will be in */var/jenkins_home/workspace*