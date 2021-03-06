.. _source:

Source Code
===========

The GeoServer source code is located on GitHub at https://github.com/geoserver/geoserver.

To clone the repository::

  % git clone git://github.com/geoserver/geoserver.git geoserver
  
To list available branches in the repository::

  % git branch
     2.1.x
     2.2.x
   * master

To switch to the stable branch::

  % git checkout 2.2.x
  
Git
---

Those coming from a Subversion or CSV background will find the Git learning curve is a steep one.
Luckily there is lots of great documentation around. Before continuing developers should take the 
time to educate themselves about git. The following are good references:

* `The Git Book <http://git-scm.com/book/>`_
* `A nice introduction <http://www.sbf5.com/~cduan/technical/git/>`_

Committing
----------

In order to commit to the repository the following steps must be taken:

#. Configure your git client for cross platform projects. See :ref:`notes <gitconfig>` below.
#. Register for commit access as described :ref:`here <comitting>`.
#. Fork the canonical GeoServer repository into your github account.
#. Clone the forked repository to create a local repository 
#. Create a remote reference to the canonical repository using a non-read only URL (``git@github.com:geoserver/geoserver.git``).

.. note::

   The next section describes how the git repositories are distributed for the project and
   how to manage local repository remote references.
   

Repository distribution
-----------------------

Git is a distributed versioning system which means there is strictly no notion of a single 
central repository, but many distributed ones. For GeoServer these are:

* The **canonical** repository located on GitHub that serves as the official authoritative 
  copy of the source code for project
* Developers' **forked** repositories on GitHub. These repositories 
  generally contain everything in the canonical repository, as well any feature or
  topic branches a developer is working on and wishes to back up or share.
* Developers' **local** repositories on their own systems.  This is where development work is actually done.

Even though there are numerous copies of the repository they can all interoperate because
they share a common history. This is the magic of git!  

In order to interoperate with other repositories hosted on GitHub, 
a local repository must contain *remote references* to them. 
A local repository typically contains the following remote references:
  
* A remote called **origin** that points to the developers' forked GitHub repository.
* A remote called **upstream** that points to the canonical GitHub repository.
* Optionally, some remotes that point to other developers' forked repositories on GitHub. 

To set up a local repository in this manner:

#. Clone your fork of the canonical repository (where "bob" is replaced with your GitHub account name)::

     % git clone git@github.com:bob/geoserver.git geoserver
     % cd geoserver
   
#. Create the ``upstream`` remote pointing to the canonical repository::

     % git remote add upstream git@github.com:geoserver/geoserver.git
    
   Or if your account does not have push access to the canonical repository use the read-only url::
    
     % git remote add upstream git://github.com/geoserver/geoserver.git

#. Optionally, create remotes pointing to other developer's forks. These remotes are typically 
   read-only::
   
      % git remote add aaime git://github.com/aaime/geoserver.git
      % git remote add jdeolive git://github.com/jdeolive/geoserver.git


Repository structure
--------------------

A git repository contains a number of branches. These branches fall into three categories:

#. **Primary** branches that correspond to major versions of the software
#. **Release** branches that are used to manage releases of the primary branches
#. **Feature** or topic branches that developers do development on

Primary branches
^^^^^^^^^^^^^^^^

Primary branches are present in all repositories and correspond to the main release streams of the 
project. These branches consist of:

* The **master** branch that is the current unstable development version of the project
* The current **stable** branch that is the current stable development version of the project
* The branches for previous stable versions

For example at present these branches are:

* **master** - The 2.3.x release stream, where unstable development such as major new features take place
* **2.2.x** - The 2.2.x release stream, where stable development such as bug fixing and stable features take place
* **2.1.x** - The 2.1.x release stream, which is at end-of-life and has no active development

Release branches
^^^^^^^^^^^^^^^^

Release branches are used to manage releases of stable branches. For each stable primary branch there is a 
corresponding release branch. At present this includes:

* **rel_2.2.x** - The stable release branch
* **rel_2.1.x** - The previous stable release branch

Release branches are only used during a versioned release of the software. At any given time a release branch
corresponds to the exact state of the last release from that branch. During release these branches are tagged.

Release branches are also present in all repositories.

Feature branches
^^^^^^^^^^^^^^^^

Feature branches are what developers use for day-to-day development. This can include small-scale bug fixes or 
major new features. Feature branches serve as a staging area for work that allows a developer to freely commit to
them without affecting the primary branches. For this reason feature branches generally only live
in a developer's local repository, and possibly their remote forked repository. Feature branches are never pushed
up into the canonical repository.

When a developer feels a particular feature is complete enough the feature branch is merged into a primary branch,
usually ``master``. If the work is suitable for the current stable branch the changeset can be ported back to the
stable branch as well. This is explained in greater detail in the :ref:`source_workflow` section.

Codebase structure
------------------

Each branch has the following structure::
  
     build/
     doc/
     src/
     data/
     

* ``build`` - release and continuous integration scripts
* ``doc`` - sources for the user and developer guides 
* ``src`` - java sources for GeoServer itself
* ``data`` - a variety of GeoServer data directories / configurations

.. _gitconfig:

Git client configuration
------------------------

When a repository is shared across different platforms it is necessary to have a 
strategy in place for dealing with file line endings. In general git is pretty good about
dealing this without explicit configuration but to be safe developers should set the 
``core.autocrlf`` setting to "input"::

    % git config --global core.autocrfl input

The value "input" essentially tells git to respect whatever line ending form is present
in the git repository.

.. note::

   It is also a good idea, especially for Windows users, to set the ``core.safecrlf`` 
   option to "true"::

      % git config --global core.safecrlf true

   This will basically prevent commits that may potentially modify file line endings.

Some useful reading on this subject:

* http://www.kernel.org/pub/software/scm/git/docs/git-config.html
* https://help.github.com/articles/dealing-with-line-endings
* http://stackoverflow.com/questions/170961/whats-the-best-crlf-handling-strategy-with-git

.. _source_workflow:

Development workflow
--------------------

This section contains examples of workflows a developer will typically use on a daily basis. 
To follow these examples it is crucial to understand the phases that a changeset goes though in the git
workflow. The lifecycle of a single changeset is:

#. The change is made in a developer's local repository.
#. The change is **staged** for commit. 
#. The staged change is **committed**.
#. The committed changed is **pushed** up to a remote repository

There are many variations on this general workflow. 
For instance, it is common to make many local commits and then push them all up in batch to a remote repository.
Also, for brevity multiple local commits may be *squashed* into a single final commit.

Updating from canonical
^^^^^^^^^^^^^^^^^^^^^^^

Generally developers always work on a recent version of the official source code. The following example 
shows how to pull down the latest changes for the master branch from the canonical repository::

  % git checkout master
  % git pull upstream master
  
Similarly for the stable branch::

  % git checkout 2.2.x
  % git pull upstream 2.2.x

Making local changes
^^^^^^^^^^^^^^^^^^^^

As mentioned above, git has a two-phase workflow in which changes are first staged and then committed 
locally. For example, to change, stage and commit a single file::

  % git checkout master
  # do some work on file x
  % git add x
  % git commit -m "commit message" x
  
Again there are many 
variations but generally the staging process involves using ``git add`` to stage files that have been added 
or modified, and ``git rm`` to stage files that have been deleted. ``git mv`` is used to move files and
stage the changes in one step.

At any time you can run ``git status`` to check what files have been changed in the working area
and what has been staged for commit. It also shows the current branch, which is useful when 
switching frequently between branches .
  
Pushing changes to canonical
^^^^^^^^^^^^^^^^^^^^^^^^^^^^

Once a developer has made some local commits they generally will want to push them up to a remote repository.
For the primary branches these commits should always be pushed up to the canonical repository. If they are for
some reason not suitable to be pushed to the canonical repository then the work should not be done on a primary
branch, but on a feature branch. 

For example, to push a local bug fix up to the canonical ``master`` branch::
  
  % git checkout master
  # make a change
  % git add/rm/mv ...
  % git commit -m "making change x"
  % git pull upstream master
  % git push upstream master
  
The example shows the practice of first pulling from canonical before pushing to it. Developers should **always** do 
this. In fact, if there are commits in canonical that have not been pulled down, by default git will not allow 
you to push the change until you have pulled those commits.

.. note:: 
   
   A **merge commit** may occur when one branch is merged with another. 
   A merge commit occurs when two branches are merged and the merge is not a "fast-forward" merge.
   This happens when the target branch has changed since the commits were created.
   Fast-forward merges are described `here <http://git-scm.com/book/en/Git-Branching-Basic-Branching-and-Merging>`_ 
   and are worth reading about. 
   
   An easy way to avoid merge commits is to do a "rebase" when pulling down changes::
   
     % git pull --rebase upstream master
     
   The rebase makes local changes appear in git history after the changes that are pulled down.
   This allows the following merge to be fast-forward. This is not a required practice since merge commits are fairly harmless, 
   but they should be avoided where possible since they clutter up the commit history and make the git log harder to read.
   
Working with feature branches
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

As mentioned before it is always a good idea to work on a feature branch and not directly on a primary branch. A classic
situation every developer who has used a version control system has run into is when a developer has 
worked on a new feature locally and made a ton of changes, but then needs to switch context to work on some other feature or 
bug fix. The developer tries to do that in the midst of the other changes and ends up committing a file they never intended
to. Feature branches are the remedy for this.

To create a new feature branch off of the master branch::

  % git checkout -b my_feature master
  % # make some changes
  % git add/rm, etc...
  % git commit -m "first part of my_feature"
  
Rinse, wash, repeat. The nice about thing about using a feature branch is that it is easy to switch context
to work on something else. Just ``git checkout`` whatever other branch you need to work on,
and then return to the feature branch when ready.

Merging feature branches
^^^^^^^^^^^^^^^^^^^^^^^^

Once a developer is done with a feature branch it must be merged into one of the primary branches and pushed up
to the canonical repository. The way to do this is with the ``git merge`` command::

  % git checkout master
  % git merge my_feature

It's as easy as that. After the feature branch has been merged into the primary branch push it up as described before::

  % git pull --rebase upstream master
  % git push upstream master
  

Porting changes between primary branches
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

Often a single change (such as a bug fix) has to be committed to multiple branches. Unfortunately primary
branches **cannot** be merged with the ``git merge`` command. Instead we use ``git cherry-pick``.

As an example consider making a change to master::

  % git checkout master
  % # make the change
  % git add/rm/etc... 
  % git commit -m "fixing bug GEOS-XYZ"
  % git pull --rebase upstream master
  % git push upstream master
  
We want to backport the bug fix to the stable branch as well. To do so we have to note the commit
id of the change we just made on master. The ``git log`` command will provide this. Let's assume the commit
id is "123". Backporting to the stable branch then becomes::

  % git checkout 2.2.x
  % git cherry-pick 123
  % git pull --rebase upstream 2.2.x
  % git push upstream 2.2.x

Cleaning up feature branches
^^^^^^^^^^^^^^^^^^^^^^^^^^^^

Consider the following situation. A developer has been working on a feature branch and has gone back 
and forth to and from it making commits here and there. The result is that the feature branch has accumulated
a number of commits on it. But all the commits are related, and what we want is really just one commit.

This is easy with git and you have two options:

#. Do an **interactive rebase** on the feature branch
#. Do a **merge with squash**

Interactive rebase
~~~~~~~~~~~~~~~~~~

Rebasing allows us to rewrite the commits on a branch, deleting commits we don't want, or merging commits that should
really be done. You can read more about interactive rebasing `here <http://git-scm.com/book/en/Git-Tools-Rewriting-History#Changing-Multiple-Commit-Messages>`_. 

.. warning::

   Much care should be taken with rebasing. You should **never** rebase commits that are public (that is, commits that have 
   been copied outside your local repository). Rebasing public commits changes branch history and results in the inability to merge
   with other repositories.
   

The following example of an interactive rebase on a feature branch::

  % git checkout my_feature
  % git log

The git log shows the current commit on the branch is commit "123". 
We make some changes and commit the result::

  % git commit "fixing bug x" # results in commit 456

Then we realized we forgot to stage a change before committing. So we add the file and commit::

  % git commit -m "oops, forgot to commit that file" # results in commit 678

Again we made a mistake, a typo, so we fix and commit again::

  % git commit -m "darn, made a typo" # results in commit #910

At this point we have three commits when what we really want is one. So we rebase specifying the 
revision before the first first commit::

  % git rebase -i 123
  
This invokes an editor that allows us to indicate which commits should be combined together.
Git *squashes* the commits into an equivalent single commit. 
Once this is done we can merge the cleaned-up feature branch into master as usual::

  % git checkout master
  % git merge my_feature

Again, be sure to read up on this feature before attempting to use it. And again, **never rebase a public commit**.

Merge with squash
~~~~~~~~~~~~~~~~~

The ``git merge`` command takes an option ``--squash`` that basically does the merge but does not commit the result 
to the target branch. This will squash all the commits from the feature branch into a single staged changeset that
is ready to be committed::

  % git checkout master
  % git merge --squash my_feature
  % git commit -m "implemented feature x"
  
  
More useful reading
-------------------

The content in this section is not intended to be a comprehensive introduction to git. There are many things not covered
that are invaluable to day-to-day work with git. Some more useful info:

* `10 useful git commands <http://about.digg.com/blog/10-useful-git-commands>`_
* `Git stashing <http://git-scm.com/book/en/Git-Tools-Stashing>`_
* `GeoTools git primer <http://docs.geotools.org/latest/developer/procedures/git.html>`_

  



