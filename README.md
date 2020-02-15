
Instrukcja uruchomienia

Projekt stworzono na systemie Ubuntu 19.10 wraz z pomocą OpenJDK 14
Plik jar serwera to `Server.jar` a klienta to `Client.jar`


Na Windows 10 do testowania był używany AdoptOpenJDK 13(https://adoptopenjdk.net/) będący darmową alternatywą dla Java JDK
Pliki wyeksportowane na Windowsie 10 mają kolejno nazwy `ServerWIN.jar` oraz `ClientWIN.jar`

## Plik Jar

Podane sposoby przetestowane zostały zarówno na Linuxie jak i Windowsie

### Uruchomienie poprzez dwuklik
Wyeksportowane pliki jar wystarczy uruchomić poprzez dwukrotne przyciśnięcie myszką oczywiście po nadaniu praw do uruchomienia pliku(chmod +x Server.jar)


Serwer nie posiada interfejsu graficznego, dlatego należy aby go wyłączyć, należy wyłączyć proces w menedżerze zadań/monitorze systemu.

Gif z uruchomieniem:

![jfile](https://user-images.githubusercontent.com/41945903/74589050-3d7d4d00-5002-11ea-88fa-9e5aac11971c.gif)


### Uruchomienie przez terminal

Drugim sposobem pokazanym na gifie jest uruchomienie gry poprzez terminal.

Najpierw przy pomocy polecenia `java -jar Server.jar` uruchamiamy serwer(błędy o błędnym zapisie nie pokazują się w Eclipse), a potem w drugim terminalu uruchamiamy klienta poleceniem `java -jar Client.jar`

Wszystko jest zawarte w tym gifie

![jfile](https://user-images.githubusercontent.com/41945903/74588938-3a359180-5001-11ea-8488-7f160433e5cb.gif)


## Uruchomienie kodu źródłowego

Kod źródłowy nie zawiera, żadnych zewnętrzych zależności, więc może być prosto uruchomiony na komputerze, tak jak jest to pokazane na gifie.

Gif z uruchomieniem(najlepiej uruchomić w nowym oknie)
![jfile](https://user-images.githubusercontent.com/41945903/74109506-527e5a00-4b84-11ea-8cb6-b80be73d80cd.gif)

## Jak działa gra?

Na początek musimy uruchomić serwer, który domyślnie nasłuchuje na porcie 9999.
Serwer oczekuje aż do zamknięcia na podłączenie się innych graczy/użytkowników

Użytkownik musi najpierw uruchomić menu GUI, w którym musi wpisać login i nacinąć na przycisk rozpoczęcia gry.
W tym momencie do serwera podłącza się gracz.
Z uwagi na to, że dużo łatwiej było nam testować grę, gdy nie musieliśmy uruchamiać drugiego programu, to stworzyliśmy metodę aby z jednego okna mogła się połączyć do gry dowolna liczba użytkowników i w zasadzie działania nie ma pomiędzy tymi metodami łączenia żadnych różnic.

Po wpisaniu loginu(walidacja nie pozwala na puste pole), otwiera się plansza sapera z określonymi wymiarami(Klient z racji bezpieczeństwa przed hackowaniem nic nie wie o tej planszy i zostaje ona do niego wysyłana za każdym razem gdy się zaktualizuje).

Serwer tworzy w tym czasie planszę o potrzebnych wymiarach/parametrach.

Gracz komunikuje serwerowi, że się połączył i czeka na gracza, a serwer po otrzymaniu transmisji również czeka na drugiego gracza.

Po podłączeniu się drugiego gracza serwer przyznaje jemu możliwość ruchu.

Klient po kliknięciu na pole przechodzi w stan oczekiwania na odpowiedź.

Serwer w tym czasie oblicza sobie pozycję bomb, odkrytych miejsc itp.

Na końcu wysyła odpowiedź do gracza, albo o tym, że przegrał, albo że ma teraz ruch.

Po zakończeniu rozgrywki blokowane są wszystkie przyciski na planszy, dokładnie tak jak ma to miejsce po zakończonym ruchu.

Dodatkowo klient od serwera jest rozłączany natychmiast po przegranej, lecz widzi ciągle stan planszy.

Klikanie jest stworzone w oparciu o klasę przycisku implementującą MouseListener, pozwalającą wykrywać kliknięcia i reagować na nie.

