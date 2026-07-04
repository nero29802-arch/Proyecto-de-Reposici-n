# Sistema de Control de Inventario

## Nombre del proyecto
Sistema de Control de Inventario — Proyecto de Reposición

## Nombre del estudiante
Jason Steve Ortiz Tenorio

## Descripción del sistema
Aplicación de escritorio desarrollada en Java Swing que permite administrar
un inventario básico de productos. Permite registrar, editar, eliminar,
buscar, filtrar, ordenar y visualizar productos, además de generar
estadísticas y exportar el inventario a un archivo CSV. El proyecto está
organizado mediante programación por capas (modelo, repositorio, negocio,
presentación, excepciones y util), maneja excepciones personalizadas y
utiliza Collections y genéricos para el almacenamiento en memoria.

## Requisitos para ejecutarlo
- JDK 8 o superior instalado.
- NetBeans (recomendado) o cualquier IDE compatible con Java.

### Ejecución desde NetBeans
1. Abrir NetBeans → File → Open Project → seleccionar la carpeta del proyecto.
2. Clic derecho sobre el proyecto → Run.

## Componentes Swing utilizados
- `JFrame` (ventana principal)
- `JMenuBar`, `JMenu`, `JMenuItem`
- `JToolBar`
- `JTabbedPane` (3 pestañas: Registro, Lista, Estadísticas)
- `JTextField`, `JTextArea`, `JComboBox`, `JCheckBox`, `JRadioButton`, `ButtonGroup`
- `JTable` con `DefaultTableModel`
- `JButton`, `JLabel`, `JScrollPane`
- `JOptionPane` (validaciones, confirmaciones, mensajes)
- `JFileChooser` (exportación de inventario)
- `JDialog` (historial de acciones)

## Collections utilizadas
- `List<Producto>` — almacena los productos registrados (capa repositorio).
- `Set<String>` — controla los códigos de producto y evita duplicados (capa repositorio).
- `Map<String, Integer>` — contabiliza la cantidad de productos por categoría (capa repositorio).
- `Stack<String>` — guarda el historial de acciones realizadas (capa negocio).
- `Comparator<Producto>` / `Collections.sort()` — ordenamientos por nombre, precio y cantidad.

## Excepciones personalizadas creadas
- `DatoInvalidoException` — datos ingresados no válidos (incluye encadenamiento
  de excepciones al capturar errores de formato numérico).
- `ProductoDuplicadoException` — intento de registrar/editar un producto con
  un código ya existente.
- `ArchivoException` — errores durante la exportación del inventario a archivo.

## Instrucciones de uso
1. Ir a la pestaña **Registro de productos** y llenar el formulario.
2. Presionar **Guardar** para registrar un nuevo producto (o **Editar** si ya
   se seleccionó uno desde la lista).
3. En la pestaña **Lista de productos** se puede buscar por nombre/código,
   filtrar por categoría, ordenar (nombre/precio/cantidad), editar o eliminar
   un producto seleccionado.
4. En la pestaña **Estadísticas** se muestran los cálculos generales del
   inventario.
5. Desde el menú **Archivo → Exportar inventario** (o el botón de la barra de
   herramientas) se exporta el inventario a un archivo `.csv` mediante
   `JFileChooser`.
6. Desde el menú **Herramientas → Ver historial** se puede consultar el
   historial de acciones (Stack).
