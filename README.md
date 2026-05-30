# Pet Adoption App — Tela Principal (Firestore dinâmico)

## Estrutura de arquivos

```
app/src/main/
├── res/
│   └── layout/
│       ├── activity_main.xml       ← Tela principal com RecyclerView
│       └── item_pet_card.xml       ← Card individual de cada pet
└── java/com/example/petadoption/
    ├── MainActivity.java           ← Activity principal
    ├── model/
    │   └── Pet.java                ← Modelo mapeado para o Firestore
    ├── repository/
    │   └── PetRepository.java      ← Todas as queries no Firestore
    └── adapter/
        └── PetAdapter.java         ← RecyclerView adapter com DiffUtil
```

---

## Estrutura esperada no Firestore

Coleção: **`pets`**

| Campo       | Tipo      | Exemplo           |
|-------------|-----------|-------------------|
| nome        | String    | "Pulga"           |
| idade       | String    | "3 anos"          |
| raca        | String    | "Vira-lata"       |
| abrigo      | String    | "Lar dos Felinos" |
| descricao   | String    | "Gata meiga..."   |
| fotoUrl     | String    | "https://..."     |
| vacinado    | Boolean   | true              |
| castrado    | Boolean   | true              |
| tamanho     | String    | "Médio"           |
| tipo        | String    | "Gato"            |

---

## Dependências (build.gradle :app)

```groovy
dependencies {
    implementation 'androidx.appcompat:appcompat:1.6.1'
    implementation 'com.google.android.material:material:1.11.0'
    implementation 'androidx.cardview:cardview:1.0.0'
    implementation 'androidx.coordinatorlayout:coordinatorlayout:1.2.0'
    implementation 'androidx.recyclerview:recyclerview:1.3.2'

    // Firebase
    implementation platform('com.google.firebase:firebase-bom:32.8.0')
    implementation 'com.google.firebase:firebase-firestore'

    // Glide (carregamento de imagens)
    implementation 'com.github.bumptech.glide:glide:4.16.0'
    annotationProcessor 'com.github.bumptech.glide:compiler:4.16.0'
}
```

---

## Ícones necessários (Vector Asset)

| Arquivo                 | Ícone Material           |
|-------------------------|--------------------------|
| `ic_chat.xml`           | `chat_bubble_outline`    |
| `ic_search.xml`         | `search`                 |
| `ic_arrow_down.xml`     | `keyboard_arrow_down`    |
| `ic_heart_outline.xml`  | `favorite_border`        |
| `ic_heart_filled.xml`   | `favorite`               |
| `ic_cat_placeholder.xml`| `pets`                   |
| `ic_nav_home.xml`       | `home`                   |
| `ic_nav_pets.xml`       | `pets`                   |
| `ic_nav_partners.xml`   | `person`                 |
| `ic_nav_register.xml`   | `article`                |
| `ic_nav_info.xml`       | `info`                   |

---

## Como funciona o fluxo

1. `MainActivity.onCreate` → chama `carregarPets()`
2. `PetRepository.observarTodos` → abre snapshot listener no Firestore
3. Qualquer mudança no banco atualiza a lista automaticamente em tempo real
4. Busca por nome → `observarPorNome()` com range query
5. Filtros → `observarComFiltros()` com `whereEqualTo`
6. `onStop()` → remove o listener para evitar vazamento de memória
