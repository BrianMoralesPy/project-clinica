# Frontend Setup — Angular

## Requisitos
- Node.js 18+
- Angular CLI 21+

## Configuración

### Instalar dependencias
```bash
cd frontend
npm install
```

### Ejecutar
```bash
ng serve
```

La app estará disponible en http://localhost:4200

### Proxy
El proxy está configurado para redirigir `/api/*` a `http://localhost:8080`.

## Estructura
```
frontend/src/app/
├── core/           # Servicios singleton, guards, interceptors
├── shared/         # Componentes reutilizables, pipes, models
├── features/       # Funcionalidades por dominio
├── layout/         # Shell de aplicación
└── app.routes.ts   # Rutas principales
```

## Convenciones
- Standalone Components (default Angular 20+)
- Signals para estado local
- RxJS para operaciones asíncrones
- Reactive Forms para formularios
- Lazy loading por feature
- OnPush change detection
