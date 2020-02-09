
Instrukcja uruchomienia

## Plik Jar
Wyeksportowane pliki jar wystarczy uruchomić poprzez dwukrotne przyciśnięcie myszką, lecz z powodu pewnego błędu, w kliencie nie wyświetlają się obrazy/obrazki.

Plik jar serwera to ServerMultiThreaded.java a klienta to ServerClient.java


## Uruchomienie kodu źródłowego

Kod źródłowy nie zawiera, żadnych zewnętrzych zależności, więc może być prosto uruchomiony na komputerze, tak jak jest to pokazane na gifie.

Gif z uruchomieniem
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

