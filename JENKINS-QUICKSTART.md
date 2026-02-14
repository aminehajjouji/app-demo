# 🚀 Guide de Démarrage Rapide - Jenkins avec GitHub Webhook

## 📋 Prérequis
- Docker et Docker Compose installés
- Compte GitHub
- Repository GitHub : https://github.com/aminehajjouji/app-demo.git

---

## 1️⃣ Démarrer l'Application (PostgreSQL + Spring Boot)

```powershell
# Démarrer l'application et la base de données
docker-compose -f compose.yaml up --build -d

# Vérifier que les services sont actifs
docker ps

# Tester l'application
curl http://localhost:8082/actuator/health
```

**Accès :**
- Application : http://localhost:8082
- PostgreSQL : localhost:5432

---

## 2️⃣ Démarrer Jenkins

```powershell
# Construire et démarrer Jenkins personnalisé
docker-compose -f docker-compose.jenkins.yml up --build -d

# Attendre 30 secondes que Jenkins démarre
Start-Sleep -Seconds 30

# Récupérer le mot de passe admin initial
docker exec jenkins-custom cat /var/jenkins_home/secrets/initialAdminPassword
```

**Accès Jenkins :** http://localhost:8081

---

## 3️⃣ Configuration Initiale de Jenkins

### A. Premier accès

1. Ouvrez http://localhost:8081
2. Collez le mot de passe admin récupéré ci-dessus
3. Cliquez sur **"Install suggested plugins"**
4. Créez votre compte admin :
   - Username : `admin`
   - Password : `admin123` (ou votre choix)
   - Full name : `Votre Nom`
   - Email : `votre@email.com`
5. Gardez l'URL Jenkins par défaut : http://localhost:8081/

---

## 4️⃣ Créer un Job Pipeline avec GitHub Webhook

### A. Créer le Job

1. Sur le tableau de bord Jenkins, cliquez sur **"New Item"**
2. Nommez le job : `app-demo-pipeline`
3. Sélectionnez **"Pipeline"**
4. Cliquez sur **OK**

### B. Configurer le Job

#### Section "General"
- ✅ Cochez **"GitHub project"**
- Project url : `https://github.com/aminehajjouji/app-demo/`

#### Section "Build Triggers"
- ✅ Cochez **"GitHub hook trigger for GITScm polling"**

#### Section "Pipeline"
- Definition : Sélectionnez **"Pipeline script from SCM"**
- SCM : Sélectionnez **"Git"**
- Repository URL : `https://github.com/aminehajjouji/app-demo.git`
- Credentials : Laissez **(none)** si le repo est public
- Branch Specifier : `*/main`
- Script Path : `Jenkinsfile`

#### Sauvegardez
- Cliquez sur **"Save"**

---

## 5️⃣ Configurer le Webhook GitHub

### A. Installer ngrok (pour exposer Jenkins en local)

```powershell
# Télécharger ngrok depuis https://ngrok.com/download
# Ou avec Chocolatey :
choco install ngrok

# Se connecter avec votre token (créez un compte gratuit sur ngrok.com)
ngrok config add-authtoken VOTRE_TOKEN_ICI

# Exposer Jenkins sur Internet
ngrok http 8081
```

**Important :** Copiez l'URL HTTPS fournie par ngrok (ex: `https://abcd-1234.ngrok.io`)

### B. Configurer le Webhook sur GitHub

1. Allez sur votre repository : https://github.com/aminehajjouji/app-demo
2. Cliquez sur **Settings** (⚙️)
3. Dans le menu de gauche, cliquez sur **Webhooks**
4. Cliquez sur **Add webhook**
5. Configurez :
   - **Payload URL** : `https://VOTRE-URL-NGROK.ngrok.io/github-webhook/`
     - ⚠️ N'oubliez pas le `/` à la fin !
   - **Content type** : `application/json`
   - **Which events** : Sélectionnez **"Just the push event"**
   - ✅ Cochez **"Active"**
6. Cliquez sur **Add webhook**

---

## 6️⃣ Tester le Pipeline

### Option A : Déclencher manuellement

1. Dans Jenkins, allez sur le job `app-demo-pipeline`
2. Cliquez sur **"Build Now"**
3. Observez le pipeline s'exécuter

### Option B : Test via Git Push (webhook)

```powershell
# Faites un petit changement dans votre code
cd C:\Users\HP\Documents\projects\app-demo

# Modifier un fichier (ex: README.md)
echo "`n# Test webhook" >> README.md

# Commit et push
git add .
git commit -m "Test webhook Jenkins"
git push origin main
```

➡️ **Le build Jenkins devrait démarrer automatiquement !**

---

## 7️⃣ Voir les Résultats

### Dans Jenkins :
- Allez sur votre job : http://localhost:8081/job/app-demo-pipeline/
- Cliquez sur le dernier build (ex: `#1`)
- Cliquez sur **"Console Output"** pour voir les logs

### Stages du Pipeline :
1. ✅ **Checkout** : Récupération du code depuis GitHub
2. ✅ **Build** : Compilation avec `mvn clean install`
3. ✅ **Test** : Exécution des tests avec `mvn test`
4. ✅ **Package** : Création du fichier JAR

---

## 8️⃣ Commandes Utiles

```powershell
# Voir les logs Jenkins
docker logs -f jenkins-custom

# Voir les logs de l'application
docker logs -f app-demo-app

# Arrêter tout
docker-compose -f compose.yaml down
docker-compose -f docker-compose.jenkins.yml down

# Redémarrer Jenkins
docker restart jenkins-custom

# Vérifier les versions dans Jenkins
docker exec jenkins-custom mvn -v
docker exec jenkins-custom java -version

# Nettoyer complètement (⚠️ supprime les données)
docker-compose -f compose.yaml down -v
docker-compose -f docker-compose.jenkins.yml down -v
```

---

## 9️⃣ Structure du Projet

```
app-demo/
├── compose.yaml                    # Docker Compose pour l'app + PostgreSQL
├── docker-compose.jenkins.yml      # Docker Compose pour Jenkins
├── Dockerfile                      # Image de l'application
├── Dockerfile.jenkins              # Image Jenkins personnalisée
├── Jenkinsfile                     # Pipeline CI/CD
├── pom.xml                         # Configuration Maven
└── src/                            # Code source Java
```

---

## 🎓 Pour les Étudiants

### Ce que ce pipeline fait :
1. **Checkout** : Récupère le code depuis GitHub
2. **Build** : Compile le projet Java avec Maven
3. **Test** : Exécute les tests unitaires
4. **Package** : Crée le fichier JAR exécutable

### Avantages du Webhook :
- ✅ Build automatique à chaque push sur GitHub
- ✅ Détection rapide des erreurs
- ✅ Pratique DevOps/CI-CD réelle
- ✅ Historique des builds

---

## 🔧 Dépannage

### Jenkins ne démarre pas ?
```powershell
docker logs jenkins-custom
```

### Le webhook ne fonctionne pas ?
1. Vérifiez que ngrok est actif : `ngrok http 8081`
2. Vérifiez l'URL dans GitHub webhook (doit finir par `/github-webhook/`)
3. Dans GitHub webhook, cliquez sur "Recent Deliveries" pour voir les erreurs

### Build échoue ?
1. Vérifiez les logs : Console Output dans Jenkins
2. Vérifiez que Maven et Java sont disponibles :
```powershell
docker exec jenkins-custom mvn -v
docker exec jenkins-custom java -version
```

---

## 📚 Ressources

- Documentation Jenkins : https://www.jenkins.io/doc/
- Documentation Spring Boot : https://spring.io/projects/spring-boot
- Documentation Maven : https://maven.apache.org/guides/
- ngrok Documentation : https://ngrok.com/docs
