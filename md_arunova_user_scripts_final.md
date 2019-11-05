# Арунова Маргарита - "RoomCreator3D"
# Пользовательские сценарии

## Группа 10МИ5
## Электронная почта: rita2002arunova@yandex.ru
## VK: vk.com/asyairita

### [Сценарий 1 - Меню]

1. При запуске пользователем приложения открывается меню.
2. Пользователь видит на экране устройства три кнопки:
   * "Создать новую комнату"
   * "Настройки"
   * "Выход".
3. Пользователь нажимает на одну из трёх кнопок меню.
4. При нажатии на кнопку "Выход" приложение закрывается.
5. При нажатии на кнопку "Настройки" запускается Сценарий 2 - Настройка приложения.
6. При нажатии на кнопку "Создать новую комнату" запускается Сценарий 3 - Создание комнаты.

### [Сценарий 2 - Настройка приложения]

1. Пользователь видит на экране устройства кнопку "настройка языка"
2. Пользователь настраивает приложение.
3. Пользователь выбирает язык
   * английский
   * русский
   * немецкий
4. При нажатии на одну из трёх кнопок приложение меняет язык.
5. Пользователь нажимает кнопку "Назад".
6. При нажатии на кнопку "Назад" все настройки сохраняются, запускается Сценарий 1 - Меню.

### [Сценарий 3 - Создание комнаты]

1. Пользователь видит на экране поле, кнопку "Редактирование" в левом нижнем углу и кнопку "Назад" в левом верхнем углу.
2. При нажатии на кнопку "Назад", если все изменения сохранены, то запускается Сценарий 1 - Меню, если какое-то изменение не сохранено, то запускается Сценарий 4 - Закончить создание комнаты.
3. При нажатии на кнопку "Редактирование" открывается окно с функциями создания комнаты:
   * импорт существующей комнаты
   * создание пола
   * создание стен
   * сохранение комнаты
4. При нажатии на кнопку для импорта существующей комнаты открывается папка с сохранёнными дизайнами комнат. Пользователь может выбрать нужный ему дизайн. Если на поле уже есть комната/пол, то высвечивается сообщение о невозможности импортировать комнату.
5. При нажатии на "создание пола", если пользователь не импортировал комнату/ещё не создавал пол, запускается Сценарий 5 - Создание пола, если импортировал/уже создал пол, то высвечивается сообщение об этом.
6. При нажатии на "создание стен", если пол не создан или стены уже созданы, высвечивается сообщение об этом, если пол создан и стены ещё не созданы, запускается Сценарий 6 - Создание стен.
7. При нажатии на "сохранение комнаты" запускается Сценарий 7 - Сохранение комнаты.

### [Сценарий 4 - Закончить создание комнаты]

1. Высвечивается диалоговое окно.
2. В диалоговом окне написано сообщение о несохраненном изменении и находятся кнопки:
   * "не сохранять изменения"
   * "сохранить изменения"
   * "отмена"
3. При нажатии на "не сохранять изменения" изменения не сохраняются, запускается Сценарий 1 - Меню.
4. При нажатии на "сохранить изменения" запускается Сценарий 7 - Сохранение комнаты.
5. При нажатии на "отмена" запускается Сценарий 3 - Создание комнаты.
6. При нажатии на "отмена" после закрытия диалогового окна все изменения, внесённые пользователем, не отменяются.

### [Сценарий 5 - Создание пола]

1. Пользователь попадает в режим редактирования пола.
2. Появляется диалоговое окно, в котором пользователь указывает длину и ширину комнаты, затем создаётся правильный четырёхугольник и появляется кнопка "редактировать пол".
3. При нажатии на кнопку "редактировать пол" появляется диалоговое окно, в котором пользователь может отредактировать пол:
   * изменить длину сторон
   * выбрать паркет (в качестве паркета пользователь может загрузить любую фотографию со своего телефона, указав размеры загруженного куска)
   * загрузить на пол модели мебели (в формате .obj), указав их размер
4. При нажатии на "выбрать паркет" открывается диалоговое окно, в котором пользователь может выбрать нужное ему изображение (в формате .png).
5. При нажатии на "добавить мебель" открывается диалоговое окно, в котором пользователь может выбрать нужную ему модель (в формате .obj).
6. При нажатии на "ОК" запускается Сценарий 3 - Создание комнаты, все внесённые изменения сохраняются.

### [Сценарий 6 - Создание стен]

1. Пользователь попадает в режим редактирования стен.
2. Появляется диалоговое окно, в котором пользователь указывает высоту стен, затем создаются стены.
3. При нажатии на стену пользователь может отредактировать её:
   * изменить размер
   * обклеить обоями (в качестве обоев пользователь может загрузить любую фотографию со своего телефона, указав размеры загруженного куска)
   * добавить на стены существующие в приложении модели мебели из общего списка или загрузить свои (в формате .obj)
4. При нажатии на "обклеить обоями" открывается диалоговое окно, в котором пользователь может выбрать нужное ему изображение (в формате .png) для каждой отдельной стены.
5. При нажатии на "добавить мебель" открывается диалоговое окно, в котором пользователь может выбрать нужную ему модель (в формате .obj) для каждой отдельной стены.
6. При нажатии на "ОК" запускается Сценарий 3 - Создание комнаты, все внесённые изменения сохраняются.

### [Сценарий 7 - Сохранение комнаты]

1. Пользователь видит диалоговое окно с полем, в которое нужно вписать имя сохраняемого файла и две кнопки "ОК" и "отмена".
2. При нажатии на "отмена" запускается Сценарий 3 - Создание комнаты.
3. При нажатии на поле ввода пользователь может указать имя файла.
4. При нажатии на "ОК", если имя файла не указано/указано неверно/уже существует файл с таким именем, высвечивается сообщение об невозможности сохранения, если все условия выполнены начинается сохранение, запускается Сценарий 3 - Создание комнаты.
5. При сохранении комнаты, дизайн, созданный пользователям преобразуется в json-формат.
6. Комната сохраняется в папку приложения в json-формате с именем, который дал пользователь (приложение при запуске запрашивает разрешение на доступ к памяти устройства, если разрешение ещё не было дано).

### [Сценарий 8 - Загрузка комнаты]

1. Пользователь видит на экране список фалов с существующем дизайном комнаты, которые он когда-то сохранил через приложение.
2. Пользователь нажимает на нужный ему файл.
3. Открывается файл с проектом.
4. Пользователь видит перед собой комнату, кнопку "Редактирование" в левом нижнем углу и кнопку "Назад".
5. Если пользователь хочет внести изменение (нажимает на кнопку "Редактирование"), запускается Сценарий 3 - Создание комнаты.
6. Если пользователь не внёс изменения и нажал на кнопку "Назад", запускается Сценарий 1 - Меню.