# Documentation des Balises EDML
Table des Matières

Conteneurs Structurels
Éléments de Contenu
Composants UI Avancés
Attributs Communs
Propriétés CSS (EDSS)


Conteneurs Structurels
<body>
Conteneur racine de l'interface. Gère le défilement global et s'adapte automatiquement à la taille de l'écran.
Classes CSS disponibles:

dark-theme - Applique un thème sombre (#1a1a1a)
light-theme - Applique un thème clair (#f5f5f5)

Attributs spéciaux:
html<body
data-layout="flow"           <!-- Type de disposition (défaut: flow) -->
data-responsive="true"       <!-- Active le responsive (défaut: true) -->
data-fullscreen="false"      <!-- Mode plein écran (défaut: false) -->
load="onLoadScript"          <!-- Script exécuté au chargement -->
unload="onUnloadScript"      <!-- Script exécuté à la fermeture -->
>
Fonctionnalités:

✅ Scrolling vertical automatique avec scrollbar
✅ Hauteur minimale = hauteur de l'écran
✅ Gestion automatique du redimensionnement
✅ Support des événements de lifecycle

Exemple:
html<body class="dark-theme" data-responsive="true">
  <header>...</header>
  <main>...</main>
  <footer>...</footer>
</body>

<div>
Conteneur générique polyvalent. C'est l'élément de base pour structurer vos interfaces.
Classes CSS disponibles:

dark-theme - Thème sombre (#2c3e50)
light-theme - Thème clair (#f8f9fa)
primary - Couleur primaire (#3498db)
success - Vert succès (#2ecc71)
danger - Rouge danger (#e74c3c)
warning - Orange avertissement
info - Bleu information
animated - Active les animations au scroll

Attributs spéciaux:
html<div
role="region"                <!-- Rôle ARIA pour l'accessibilité -->
data-text="Texte"            <!-- Contenu textuel -->
data-tooltip="Info"          <!-- Info-bulle au survol -->
data-theme="primary"         <!-- Thème spécifique -->
data-animation="fade"        <!-- Type d'animation -->
data-delay="200"             <!-- Délai d'animation (ms) -->
>
Fonctionnalités:

✅ Support des événements click et drag
✅ Virtualisation automatique pour optimisation
✅ Thèmes automatiques selon les classes
✅ Animations au scroll (si classe animated)

Exemple:
html<div class="primary" data-text="Bonjour le monde" data-tooltip="Ceci est une info-bulle">
  <!-- Enfants optionnels -->
</div>

<header>
En-tête de page ou de section. Généralement utilisé pour le titre et la navigation.
Classes CSS disponibles:

site-header - En-tête de site principal
custom-header - En-tête personnalisé (désactive le style par défaut)
dark-theme - Thème sombre (#2c3e50)
primary - Couleur primaire (#3498db)

Attributs spéciaux:
html<header role="banner">  <!-- Role ARIA (défaut: banner) -->
Comportement par défaut:

Si role="banner" ET pas de classe site-header : couleur de fond #34495e
Adapte automatiquement le thème selon les classes

Exemple:
html<header class="site-header" role="banner">
  <div data-text="Mon Application"></div>
</header>

<main>
Zone de contenu principal de la page.
Classes CSS disponibles:

custom-main - Style personnalisé (désactive le fond blanc par défaut)
dark-theme - Thème sombre (#2c3e50)
light-theme - Thème clair (#ffffff)
primary - Couleur primaire (#ecf0f1)

Attributs spéciaux:
html<main role="main">  <!-- Role ARIA (défaut: main) -->
Comportement par défaut:

Fond blanc par défaut (sauf si custom-main)

Exemple:
html<main>
  <section>
    <!-- Contenu principal -->
  </section>
</main>

<footer>
Pied de page contenant généralement des informations secondaires.
Classes CSS disponibles:

site-footer - Pied de page de site
custom-footer - Style personnalisé
dark-theme - Thème sombre (#34495e)
info - Information (#5bc0de)

Attributs spéciaux:
html<footer role="contentinfo">  <!-- Role ARIA (défaut: contentinfo) -->
Comportement par défaut:

Si role="contentinfo" : couleur de fond #95a5a6

Exemple:
html<footer class="site-footer">
  <div data-text="© 2025 Mon Application"></div>
</footer>

<section>
Section thématique de contenu. Adaptable selon le rôle ARIA.
Classes CSS disponibles:

dark-theme - Thème sombre (#2c3e50)
light-theme - Thème clair (#f8f9fa)
primary - Primaire (#3498db)
success - Succès (#2ecc71)
danger - Danger (#e74c3c)
warning - Avertissement (#f39c12)
info - Information (#17a2b8)
animated - Active les animations

Attributs spéciaux:
html<section
role="region"              <!-- Role ARIA -->
data-tooltip="Info"        <!-- Info-bulle -->
data-theme="primary"       <!-- Thème -->
>
Rôles ARIA supportés:

region - Région générique (#f8f9fa)
banner - Bannière (#343a40)
navigation - Navigation (#495057)
main - Contenu principal (#ffffff)
complementary - Contenu complémentaire (#e9ecef)
contentinfo - Informations (#6c757d)
search - Zone de recherche (#fff3cd)
form - Formulaire (#f8f9fa)

Fonctionnalités:

✅ Couleur automatique selon le rôle
✅ Support des animations
✅ Info-bulles

Exemple:
html<section role="region" class="primary">
  <div data-text="Contenu de la section"></div>
</section>

Éléments de Contenu
<text>
Élément de texte simple. Calcule automatiquement sa taille.
Fonctionnalités:

✅ Calcul automatique de la largeur optimale
✅ Retour à la ligne automatique
✅ Hauteur adaptative selon le contenu
❌ Ne peut pas avoir d'enfants

Utilisation:
html<text>Voici un texte simple</text>
Remarque: Le contenu textuel est défini directement entre les balises.

<img>
Élément d'image avec support des transformations.
Attributs:
html<img 
  src="chemin/image.png"     <!-- Chemin de l'image (requis) -->
  alt="Description"          <!-- Texte alternatif -->
  width="100"                <!-- Largeur en pixels -->
  height="100"               <!-- Hauteur en pixels -->
/>
Chemins d'images:

Chemins relatifs : /images/logo.png → charge depuis resources/hms/img/images/logo.png
URLs externes : affiche le texte alternatif (pas de chargement distant)

Fonctionnalités:

✅ Rotation possible (via code Java)
✅ Effet miroir horizontal (via code Java)
✅ Teinte de couleur (via code Java)
✅ Taille par défaut: 64x64
❌ Ne peut pas avoir d'enfants

Exemple:
html<img src="/logo.png" alt="Logo de l'application" width="128" height="128"/>

Composants UI Avancés
<virtual-list>
Liste virtualisée pour afficher efficacement de grandes quantités de données.
Attributs:
html<virtual-list
data-for="item in items"        <!-- Boucle sur la liste -->
data-item-height="40"           <!-- Hauteur d'un item (défaut: 40) -->
data-template="{{item}}"        <!-- Template d'affichage -->
style="width: 100%; height: 400px;"
>
</virtual-list>
Fonctionnalités:

✅ Rendu uniquement des éléments visibles
✅ Scrollbar intégrée automatique
✅ Support des grandes listes (500k+ items)
✅ Clipping intelligent des items
✅ Data binding avec ObservableList
✅ Mise à jour automatique lors de changements

Variables disponibles dans le template:

{{item}} - L'élément courant
{{index}} - L'index de l'élément

Exemple complet:
html<virtual-list
id="myList"
data-for="item in testItems"
data-item-height="50"
data-template="Item: {{item}} ({{index}})"
style="width: 100%; height: 500px; background-color: white;"
>
</virtual-list>
Liaison avec Java:
java// Dans votre écran
@Override
protected void initializeData() {
    BindingContext context = getBindingContext();
    ObservableList<String> items = context.createObservableList("testItems");

    for (int i = 1; i <= 100000; i++) {
        items.add("Element " + i);
    }
}

<scrollbar>
Barre de défilement personnalisable (généralement automatique dans body et virtual-list).
Attributs:
html<scrollbar 
  data-orientation="vertical"          <!-- vertical ou horizontal -->
  data-scrollbar-width="8"             <!-- Largeur (défaut: 8) -->
  data-track-color="#e0e0e0"           <!-- Couleur de la piste -->
  data-thumb-color="#888888"           <!-- Couleur du curseur -->
  data-thumb-hover-color="#666666"     <!-- Couleur au survol -->
  data-min-thumb-size="20"             <!-- Taille min du curseur -->
>
</scrollbar>
Fonctionnalités:

✅ Orientation verticale ou horizontale
✅ Drag & drop du curseur
✅ États hover
✅ Calcul automatique de la taille du curseur

Remarque: Rarement utilisé directement, car intégré automatiquement dans <body> et <virtual-list>.

Attributs Communs
Attributs de base
Tous les composants supportent ces attributs:
htmlclass="classe1 classe2"         <!-- Classes CSS -->
id="monId"                       <!-- Identifiant unique -->
style="propriété: valeur;"       <!-- Styles inline -->
Attributs de données
Préfixe data-* pour données personnalisées:
htmldata-bind="{{variable}}"         <!-- Data binding -->
data-visible="{{isVisible}}"     <!-- Visibilité conditionnelle -->
data-class="{{dynamicClass}}"    <!-- Classe dynamique -->
data-style="{{dynamicStyle}}"    <!-- Style dynamique -->
data-click="handleClick"         <!-- Gestionnaire de clic -->
data-text="{{text}}"             <!-- Contenu textuel dynamique -->
Attributs d'événements
htmlonclick="handler"                <!-- Clic -->
onhover="handler"                <!-- Survol -->

Propriétés CSS (EDSS)
Couleurs
cssbackground-color: #ffffff;       /* Couleur de fond */
color: #000000;                  /* Couleur du texte */
Formats supportés:

Hexadécimal: #RGB, #RRGGBB, #RGBA, #RRGGBBAA
Noms: red, blue, transparent, etc. (250+ couleurs)

Dimensionnement
csswidth: 100px;                    /* Largeur */
height: 50px;                    /* Hauteur */
Unités supportées:

px - Pixels (100px)
% - Pourcentage du parent (50%)
vw - Pourcentage de la largeur viewport (50vw)
vh - Pourcentage de la hauteur viewport (100vh)

Espacement
css/* Margin (externe) */
margin: 10px;                    /* Tous les côtés */
margin: 10px 20px;               /* Vertical Horizontal */
margin: 10px 20px 30px;          /* Haut H/G Bas */
margin: 10px 20px 30px 40px;     /* Haut Droite Bas Gauche */
margin-top: 10px;
margin-right: 20px;
margin-bottom: 30px;
margin-left: 40px;

/* Padding (interne) */
padding: 10px;                   /* Même syntaxe que margin */
padding: 10px 20px;
padding: 10px 20px 30px;
padding: 10px 20px 30px 40px;
padding-top: 10px;
padding-right: 20px;
padding-bottom: 30px;
padding-left: 40px;
Sélecteurs
css/* Par balise */
div { }

/* Par classe */
.ma-classe { }

/* Par ID */
#mon-id { }

Exemples Complets
Interface Simple
main_menu.edml:
html<body class="dark-theme">
  <header role="banner">
    <div data-text="Mon Application" style="padding: 20px;"></div>
  </header>

  <main>
    <section class="primary" style="margin: 20px; padding: 15px;">
      <div data-text="Bienvenue"></div>
    </section>
  </main>

  <footer>
    <div data-text="© 2025" style="padding: 10px;"></div>
  </footer>
</body>
main_menu.edss:
cssbody {
  width: 100%;
  height: 100vh;
}

header {
background-color: #2c3e50;
color: white;
height: 60px;
}

main {
width: 100%;
padding: 20px;
}

footer {
background-color: #34495e;
color: white;
height: 40px;
}
Liste Virtualisée
list_demo.edml:
html<body>
  <header style="height: 60px; background-color: #3498db;">
    <div data-text="Liste de 100k éléments" style="padding: 20px; color: white;"></div>
  </header>

  <main style="padding: 20px;">
    <virtual-list 
      id="bigList"
      data-for="item in items"
      data-item-height="50"
      data-template="{{item}}"
      style="width: 100%; height: 600px; background-color: white;"
    >
    </virtual-list>
  </main>
</body>
list_demo.edss:
cssbody {
  width: 100%;
  height: 100vh;
  background-color: #ecf0f1;
}
ListDemoScreen.java:
javapublic class ListDemoScreen extends StandardResourceEdmlScreen {
    public ListDemoScreen() {
        super(Text.literal("Liste Demo"), "list_demo");
    }

    @Override
    protected void initializeData() {
        BindingContext context = getBindingContext();
        ObservableList<String> items = context.createObservableList("items");
        
        for (int i = 1; i <= 100000; i++) {
            items.add("Élément #" + i);
        }
    }
}