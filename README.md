# About BiblioStock
//TODO

# Help
To clone the repo
```
git clone -b <branch-name> <repository-url>
//e.g. git clone -b dev/models https://github.com/CSIS3275-inclass/bibliostock.git

```

If you're making any change

```
//create your own dev branch 
git branch <name>
git checkout <created branch name> #switch to your new branch
//after you are satisfied with your changes
git add -A //this will all the changes in one commit
git commit -m "<your commit message>"

//alternatively to separate your changes in multiple commits you can run (for each of them)
git add <paths of specific files you want to commit>
git commit -m "<message for that commit>"

//when all changes are staged for commit(s) you can push them to the repo
git push origin <your branch>

//you should now see your new branch in the repo
//later, when your code has been reviewed/discusse you can make a pull request to merge your branch with the main dev branch, then when main dev branch looks good it can be merged to main branch
```

other useful commands
```
git --help //help
git help <command> //help with specific command/
git branch -v //list all the branches in your local repo
git status //list all files with new changes
git diff //see all changes
#or git diff <path> to see changes on a specific file
git restore <path> //to unstage change
git log //see commit history
```

## Resources 
**Git**
- [Install git](https://www.atlassian.com/git/tutorials/install-git)
- [User Manual](https://git-scm.com/docs/user-manual)
- [References](https://git-scm.com/docs)
- [Collaborating with pull requests](https://docs.github.com/en/pull-requests/collaborating-with-pull-requests)

**SpringJPA**
- [JPA repositories - Supported keywords inside method names](https://docs.spring.io/spring-data/jpa/docs/1.5.0.RELEASE/reference/html/jpa.repositories.html#:~:text=Table%C2%A02.3.%C2%A0Supported%20keywords%20inside%20method%20names)
- Manipulate how JPA queries the database with the naming of the method
e.g.
```
Set<Course> findBySectionID(blabla)
// query path Course class -> Section class > its ID property 
```

# Recommended practices
(Notes from class)
## In model classes
- For sets/array/list of properties to avoid instantiating objects for each instances -> `Fetch.TYPE Lazy`
- For single properties (foreign keys) it's ok to set `Fetch.TYPE eager`
- Add `jsonproperty` to nested properties to let JPA know which class it should refer to when converting it from json to entity object in api controller
- Add `jsonignore` to relationship-related properties

