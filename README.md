# Attention

Il n'est pas impossible que l'application rencontre un problème et n'affiche pas certaines informations importantes, en utilisant cette application, vous acceptez le risque de rater des cours et êtes responsable de vos absences. Si vous partagez l'application, vous devez les avertir également, car vous pourrez être responsable.

# Contribuer à l'application emploi du temps

Ce projet est open source et peut servir de projet d'initiation au développement mobile avec Kotlin. 
Pour contribuer à ce projet, vous devez d'abord fork le projet sur GitHub, cela vous permettra d'avoir une copie du projet sur votre compte que vous pourrez modifier.

Lorsque vous voulez ajouter des fonctionnalités ou modifier une partie du code, il est recommandé de créer une nouvelle branche avec le nom du changement. Une fois la fonctionnalité prête, faites une pull request de cette branche sur la branche développement du projet original. Cela aura pour effet de commencer une procédure qui permettra à tout le monde de tester vos changements et d'en discuter. Si tout se déroule bien, les commits de cette branche seront apportés à la branche développement.

Pour le développement de cette application, il est recommandé d'utiliser Android Studio, la suite de cette documentation est destinée aux développeurs utilisant Android Studio.

## Structure de l'application

L'application est répartie en plusieurs parties distinctes qui ont différentes utilités et formats, cette section est dédiée à résumer leurs usages et comment interagir avec ces parties.

### Configuration (Gradle Scripts)

Cette section est réservée à la configuration du projet comme les plugins et leurs versions (bien que ceux-ci ne devraient pas être modifiés sans raison) ou encore le lien vers l'API qui se trouve dans les variables productionBaseUrl et developmentBaseUrl dans le fichier build.gradle.kts (Module :app). 

### Ressources

Le dossier res dans app permet de définir des ressources comme des couleurs, des chaînes de caractères et leurs traductions, des icônes, mais surtout les layouts qui définissent la mise en page.

#### Drawable

Le dossier drawable contient l'ensemble des icônes et peut contenir d'autres éléments classés comme tel. 

Pour importer une icône, il faudra d'abord avoir un fichier en format SVG. Ensuite, appuyez sur le + en haut de votre explorateur de fichier ou fichier > nouveau et sélectionnez vector asset puis local file et définissez une taille appropriée comme 24 x 24 dp et validez. 
Il est possible d'utiliser l'option clip art qui permet de générer une icône grâce à la bibliothèque Android.

#### Layout

Ce dossier regroupe toutes les structures des fragments qui agissent comme des pages, ainsi que des structures d'autres éléments pouvant être générés au sein des pages.

Pour ajouter des layouts, suivez la même méthode que pour une icône, mais sélectionnez Android resource file, choisissez layout dans le type de ressource et définissez un élément racine. La structure est très similaire à du HTML, mais les éléments ne sont pas les mêmes, vous devrez vous inspirer des layouts déjà présents et essayer de nouveaux éléments qui pourraient correspondre au besoin.

À noter que toute taille exprimée en dp (density pixels) sera automatiquement ajustée en fonction de l'appareil.

#### Navigation

Le fichier nav_graph.xml qui y est contenu permet de définir les chemins de navigation entre les différents fragments et les arguments pouvant y être passés. 
Ce fichier n'est pas nécessaire pour le moment, mais n'hésitez pas à demander de l'aide si vous voulez ajouter des pages.

#### Values

Les fichiers contenus dans ce dossier agissent comme constantes, que ce soit des couleurs ou des chaînes de caractères. Ces valeurs sont utilisées dans les layouts par exemple.

### Fichiers de code

Le code qui permet de faire fonctionner l'application se trouve dans app > kotlin+java > com.iutmetz.edt, je vais rapidement expliquer la responsabilité de chaque grande partie, plus d'informations se trouvent dans les fichiers eux même en commentaire.

#### Data

Cette partie sert à définir les éléments de la base de données locale, le modèle des données serveur, des classes de données et des repositories qui agissent comme l'interface de gestion des données locale et serveur.

#### Di

Cette partie contient les modules qui mettent à disposition des objets grâce au plugin dagger qui injecte automatiquement des classes depuis la fonction correspondante définie dans les modules.

#### Ui

Ce dossier contient tout le code relié aux différentes pages, pour créer une nouvelle page, copiez seulement le code déjà présent, inspirez-vous de la forme et changez le fonctionnement du fragment. Il n'est pas nécessaire de modifier les fichiers BaseFragment et MainActivity.

#### Util

Cette partie regroupe des objets qui servent dans certains cas, comme mettre à disposition des fonctions dans tous les fichiers.

## Lancer/Tester l'application

Pour lancer l'application, il faut d'abord sélectionner la variante voulue dans le menu de gauche > ... > Build Variant. Il devrait y avoir development/production debug/release, sélectionnez developmentDebug pour utiliser les variables de développement et permettre le debuggage, sinon productionRelease pour les valeurs de production sans debuggage.

Ensuite, si vous avez sélectionné une variante debug, vous pouvez démarrer l'app grâce au bouton debug en haut de la fenêtre, sinon pour la variante productionRelease il vous faudra générer un APK (build > generate signed app bundle or APKs) ou lancer l'application sans debug.

Il est conseillé d'utiliser un téléphone lié à l'application, car l'émulateur consomme beaucoup de ressources.
