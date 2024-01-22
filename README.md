## Mobil- és webes szoftverek
### 2023. 10. 15
### ToDoApp (TaskTracker)
### Muzslai László

## Bemutatás

Szerettem volna egy alkalamzást amiben egy helyen tudom tárolni az egyetmi feladatokat. Sok beadandó miatt nehéz fejben tartani, hogy mit meddig kell beadni, és hogy egyáltalán megcsináltam-e már a feladatot. A célközönsség bárki lehet, aki szeretné egy helyen átlátni a feladatait.

## Főbb funkciók

Az alkalmazás fragment alapú tecnológiával működik. A fő képernyőn lehet választani két opció közül: Taskok listázása, illetve új Task felvétele. Utóbbi esetében egy formot látunk, ahol három szöveges beviteli mező van, ebből az első kettő kötelező: Subject és Title. A Description mező nem szükséges. Van egy DatePickerDialog, ahol a határidőt lehet kiválasztani az adott Taskhoz, ez is kötelező mező. A SUBMIT gomb megnyomásával az adatok bekerülnek egy SQLite adatbázisba ROOM ORM API segítségével. Itt van lehetőség visszavonni a Taskot.

A másik fülön alapértelmezett módban az összes Task látszik növekvő határidővel. Lehet szűrni egy Toolbar menüben a Done/Pending/All Task opciók között, valamint 7, 3, 2 és 1 napos határidők között is. Ezek a funkciók külön-külön működnek. A menüben van még egy Delete all opció is.
A Taskok egyessével is törölhetők, és beállítható rajtuk az elvégzetsséget jelentő pipa is. Exportálhatók a naptárba Content Providerek segítségével.
 
## Választott technológiák:

- UI
- fragmentek
- RecyclerView
- Perzisztens adattárolás
- Content Provider/Content Resolver használata (naptár)


# Házi feladat dokumentáció (ha nincs, ez a fejezet törölhető)
Az alkalmazás a Főbb funkciók részben leírtakat telejesítettem. Az adatok tárolása egy SQLite adatbázisban történik, a készüléken. Ezt a Google által létrehozott ROOM ORM keretrendszer segítségével készítettem el. Az adatbázis sémát a Task.kt fájlban definiátam, a lekérdezéseket és az adatmanipulációt pedig a TaskDAO.kt-ban. A projekt ebből az adatbázisból dolgozik.

Egy érdekes funkció, hogy a Taskokat lehet a naptárba exportálni. Ez szükséges volt egy WRITE_CALENDAR engedélyre, amit a manifest fájlban rögzíetni kell, illetve futási időben el kell kérni ezt az engedélyt a felhasználótól kódból. Ezt követően a Calendar által biztosított content providert felhasználva, pontosabban egy content resolver segítségével tudunk a Calendar adatbáziában rögzíteni egy új bejegyzést rögzíetni.

Ezen kívül egy nav graph segítségével oldottam meg a fragmentek közötti képernyő váltásokat.

Az ikonokat a beépített Image illetve Vector asset segítségével készíttem, továbba a VIK logót az [Assert studio](https://romannurik.github.io/AndroidAssetStudio/) segítségével.

A recycle view a laborhoz hasonlóan készült, lista elem layoutja saját készítésű, egyes mezők használják az [AutoFitTextView](https://github.com/grantland/android-autofittextview) könyvtárat, hogy a hosszab szövegek is kiférjenek, ellenben így is korlátozott a megjeleníthető karakterek száma, hogy valamelyeset még olvasható maradjon, és normálisan el lehessen helyezni a listában az összes elemet. Az exportálás után láthatóvá válik a hosszabb szöveg is.

A kölünboző műveletek ami több elemet is érint egy Toolbar menüelemeiként jelenik meg.