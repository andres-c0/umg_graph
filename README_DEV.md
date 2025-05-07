# 🛠️ Guía Diaria de Trabajo con Git (README_DEV.md)

Esta guía está diseñada para que puedas trabajar de forma ordenada y eficiente en tu proyecto usando Git y GitHub bajo la metodología **Git Flow**.

---

## 🚀 Al iniciar tu día de trabajo

1. Navegá a tu repositorio local:
   ```bash
   cd ruta/del/repositorio
   ```

2. Traé todos los cambios del remoto:
   ```bash
   git fetch --all
   ```

3. Actualizá tu rama base `develop`:
   ```bash
   git checkout develop
   git pull origin develop
   ```

4. Cambiá o creá tu rama de trabajo:
   ```bash
   git checkout -b feature/nueva_funcionalidad_etl
   # o si ya existe:
   git checkout feature/nueva_funcionalidad_etl
   git pull origin feature/nueva_funcionalidad_etl
   ```

---

## ✍️ Durante el trabajo

- Hacé cambios y registrálos con commits significativos:
  ```bash
  git add archivo_modificado.py
  git commit -m "Mensaje descriptivo del cambio"
  ```

- Para mantener tu rama alineada con `develop`:
  ```bash
  git pull origin develop
  git merge develop
  ```

---

## ✅ Al finalizar o subir cambios

1. Asegurate de estar en tu rama de trabajo:
   ```bash
   git checkout feature/nueva_funcionalidad_etl
   ```

2. Subí los cambios al repositorio remoto:
   ```bash
   git push origin feature/nueva_funcionalidad_etl
   ```

3. (Opcional) Abrí un Pull Request en GitHub hacia `develop`

4. Si finalizaste esa funcionalidad:
   - Mergeá a develop:
     ```bash
     git checkout develop
     git pull origin develop
     git merge feature/nueva_funcionalidad_etl
     git push origin develop
     ```

   - Eliminá la rama:
     ```bash
     git branch -d feature/nueva_funcionalidad_etl
     git push origin --delete feature/nueva_funcionalidad_etl
     ```

---

## 🧠 Tips adicionales

- No trabajes en `main` directamente. 
- Usá `develop` como base para tus features.
- Hacé commits pequeños y descriptivos.
- Siempre hacé `pull` antes de hacer `push` o merge.
- Usá nombres claros para tus ramas: `feature/facebook_etl`, `hotfix/arreglo_fecha`, etc.

---

Con esta rutina mantenés tu repo limpio, ordenado y profesional ✨