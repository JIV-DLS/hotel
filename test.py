import requests
import json
from datetime import datetime, timedelta

from decimal import Decimal, ROUND_CEILING

SWISS_FRANC = 'SWISS_FRANC'

YEN = 'YEN'

POUND_STERLING = 'POUND_STERLING'

DOLLAR = 'DOLLAR'

EURO = 'EURO'

NUMBER_OF_NIGTH_TO_DO_IN_ROOM_1 = 7

NUMBER_OF_NIGHTS_IN_ROOM_TYPE_ONE = 3
NUMBER_OF_NIGHTS_IN_ROOM_TYPE_TWO = 3

# Set the base URL for the Spring Boot API
base_url = "http://localhost:8081"


# Scenario 1: Create a new client
def create_client(full_name="John Doe", email="john.doe@example.com", phone_number="+1234567890"):
    endpoint = "/clients"
    client_data = {
        "fullName": full_name,
        "email": email,
        "phoneNumber": phone_number
    }
    response = requests.post(base_url + endpoint, json=client_data)
    # print("Scenario 1 - Create Client:")
    created_client = response.json()
    # print(created_client)
    return created_client


def credit_account(client_email, amount, currency=DOLLAR):
    endpoint = f"/clients/{client_email}/credit/{amount}/{currency}"
    response = requests.get(base_url + endpoint)
    # print("Scenario - Credite client account:")
    credited = response.json()
    # print(credited)
    return credited


def refund_account(client_email):
    endpoint = f"/clients/{client_email}/refund"
    response = requests.get(base_url + endpoint)
    # print("Scenario - Credite client account:")
    credited = response.json()
    # print(credited)
    return credited


def get_account_balance(client_email, currency=DOLLAR):
    endpoint = f"/clients/{client_email}/balance/{currency}"
    response = requests.get(base_url + endpoint)
    # print("Scenario - Credite client account:")
    credited = response.json()
    # print(credited)
    return credited


# Scenario 2: Fetch clients
def fetch_clients():
    endpoint = "/clients"
    response = requests.get(base_url + endpoint)
    # print("\nScenario 2 - Fetch Clients:")
    # print(response.json())


# Scenario 3: Make a reservation
def make_reservation(client_email, room_type, number_of_nights=3, in_x_days=7):
    endpoint = "/reservations"
    reservation_data = {
        "clientId": client_email,  # Replace with an actual client ID
        "roomType": room_type,  # Replace with actual room IDs
        "checkInDate": get_day_in_x_days(in_x_days),  # Replace with
        "numberOfNights": number_of_nights
    }
    response = requests.post(base_url + endpoint, json=reservation_data)
    # print("\nScenario 3 - Make Reservation:")
    reservation_done = response.json()
    # print(reservation_done)
    return reservation_done


def get_day_in_x_days(in_x_days):
    return (datetime.now().date() + timedelta(days=in_x_days)).isoformat()


# Scenario 3: Make a reservation
def confirm_reservation(reservationId):
    endpoint = f"/reservations/{reservationId}/confirm"
    response = requests.get(base_url + endpoint)
    # print("\nScenario - Reservation confirmed:")
    # print(reservation_confirmed)
    return response


# Scenario 4: Fetch reservations
def fetch_reservations():
    endpoint = "/reservations"
    response = requests.get(base_url + endpoint)
    # print("\nScenario 4 - Fetch Reservations:")
    return response.json()


# Scenario 4: Fetch reservations
def get_reservation(reservation_id):
    endpoint = f"/reservations/{reservation_id}"
    response = requests.get(base_url + endpoint)
    # print("\nScenario 4 - Fetch Reservations:")
    return response.json()


# Scenario 4: Fetch reservations
def cancel_reservation(reservation_id):
    endpoint = f"/reservations/{reservation_id}/cancel"
    response = requests.get(base_url + endpoint)
    # print("\nScenario 4 - Fetch Reservations:")
    return response.json()


# Scenario 5: Fetch rooms
def fetch_rooms():
    endpoint = "/rooms"
    response = requests.get(base_url + endpoint)
    # print("\nScenario 5 - Fetch Rooms:")
    rooms = response.json()
    # print(rooms)
    return rooms


def assert_and_print(condition, message):
    assert condition, message
    print("Assertion succeeded:", message)


NUMBER_OF_NIGHTS_IN_THE_FIRST_ROOM = 3

EXCHANGE_RATES_TO_EURO = {
    EURO: Decimal('1'),
    DOLLAR: Decimal('1.12'),  # Example rate: 1 USD = 0.89 EUR
    POUND_STERLING: Decimal('0.85'),  # Example rate: 1 GBP = 1.18 EUR
    YEN: Decimal('130.71'),  # Example rate: 1 JPY = 0.0077 EUR
    SWISS_FRANC: Decimal('1.06')  # Example rate: 1 CHF = 0.94 EUR
}

# Example usage
amount_in_dollars = Decimal('100')
euro_equivalent = amount_in_dollars / EXCHANGE_RATES_TO_EURO[DOLLAR]


# print(f'{amount_in_dollars} USD is approximately {euro_equivalent:.2f}')

def scenario_1():
    print("""   _____ _____________   _____    ____  ________     ___
  / ___// ____/ ____/ | / /   |  / __ \/  _/ __ \   <  /
  \__ \/ /   / __/ /  |/ / /| | / /_/ // // / / /   / / 
 ___/ / /___/ /___/ /|  / ___ |/ _, _// // /_/ /   / /  
/____/\____/_____/_/ |_/_/  |_/_/ |_/___/\____/   /_/   
                                                        
An american user created an account, recharging with dollars it's account to take the room of type STANDARD,
 as Euro is upper than Dollar it recharge it's account for 4 night to be sure that it's reservation will be done
 it reserve the room for 3 night in 7 days and confirm the same day
 and as the event for which he came for was cancelled, he cancel the reservation
""")
    client_json = {
        "fullName": 'jack',
        "email": 'jack@gmail.com',
        "phoneNumber": '+33652689871'
    }
    # Execute the scenarios
    created_client = create_client(client_json['fullName'], client_json['email'], client_json['phoneNumber'])
    assert_and_print(created_client, 'client created')
    assert_and_print(created_client['fullName'] == client_json['fullName'],
                     'with the client full name {}'.format(client_json['fullName']))
    assert_and_print(created_client['email'] == client_json['email'],
                     'with the client email {}'.format(client_json['email']))
    assert_and_print(created_client['phoneNumber'] == client_json['phoneNumber'],
                     'with the client phone number {}'.format(client_json['phoneNumber']))

    refund_account(created_client['email'])

    rooms = fetch_rooms()
    assert_and_print(len(rooms) == 3, 'room types is 3')
    initial_balance = get_account_balance(created_client['email'], DOLLAR)

    assert_and_print(
        initial_balance == 0, 'client balance is {}'.format(initial_balance))

    # Because Dollar is lower than Euros as Room prices are in Euros
    assert_and_print(
        credit_account(created_client['email'], rooms[0]["pricePerNight"] * (NUMBER_OF_NIGHTS_IN_ROOM_TYPE_ONE + 1),
                       DOLLAR) == True, 'account credited')
    balance_after_first_credit = get_account_balance(created_client['email'], DOLLAR)
    # result.quantize(Decimal('1.'), rounding=ROUND_CEILING)
    # (Decimal(balance_after_first_credit - initial_balance)*EXCHANGE_RATES_TO_EURO[DOLLAR]).quantize(Decimal('1.'), rounding=ROUND_CEILING), ((rooms[0]["pricePerNight"] * (NUMBER_OF_NIGHTS+1))*EXCHANGE_RATES_TO_EURO[DOLLAR]).quantize(Decimal('1.'), rounding=ROUND_CEILING)
    # Decimal(balance_after_first_credit - initial_balance)*EXCHANGE_RATES_TO_EURO[DOLLAR], (rooms[0]["pricePerNight"] * (NUMBER_OF_NIGHTS+1))*EXCHANGE_RATES_TO_EURO[DOLLAR]
    assert_and_print(round(balance_after_first_credit - initial_balance) == (
            rooms[0]["pricePerNight"] * (NUMBER_OF_NIGHTS_IN_ROOM_TYPE_ONE + 1)),
                     f'account credited with the given amount {round(balance_after_first_credit - initial_balance)} in {DOLLAR}')
    reservation_1 = make_reservation(created_client['email'], rooms[0]['type'], NUMBER_OF_NIGHTS_IN_ROOM_TYPE_ONE,
                                     in_x_days=NUMBER_OF_NIGTH_TO_DO_IN_ROOM_1)
    assert_and_print(reservation_1['room'] == rooms[0], f'reservation done with the room {rooms[0]}')
    assert_and_print(reservation_1['client']['email'] == created_client['email'],
                     'reservation done with the mail {}'.format(created_client['email']))
    assert_and_print(get_day_in_x_days(NUMBER_OF_NIGTH_TO_DO_IN_ROOM_1) == reservation_1['checkInDate'],
                     'reservation is done unitl {}'.format(reservation_1['checkInDate']))
    assert_and_print(reservation_1['status'] == 'REGISTERED',
                     'reservation is in status {}'.format(reservation_1['status']))
    assert_and_print(reservation_1['totalAmount'] == rooms[0]['pricePerNight'] * NUMBER_OF_NIGHTS_IN_ROOM_TYPE_ONE,
                     'reservation done with the mail {}'.format(created_client['email']))
    assert_and_print(reservation_1['confirmationDate'] == None, 'reservation not yet confirmed')
    balance_after_reservation_for_room_1 = get_account_balance(created_client['email'], DOLLAR)
    assert_and_print(abs(Decimal(balance_after_reservation_for_room_1) - round(
        Decimal(balance_after_first_credit) - (reservation_1['totalAmount'] // 2) * EXCHANGE_RATES_TO_EURO[DOLLAR],
        2)) < 1e-10, 'the half of {} is debited suucessfully'.format(
        (reservation_1['totalAmount'] // 2) * EXCHANGE_RATES_TO_EURO[DOLLAR]))
    assert_and_print(confirm_reservation(reservation_1['id']).json() == True, 'reservation is confirmed')
    balance_after_confirmation_of_room_1 = get_account_balance(created_client['email'], DOLLAR)
    assert_and_print(abs(Decimal(balance_after_confirmation_of_room_1) - round(
        Decimal(balance_after_reservation_for_room_1) - (reservation_1['totalAmount'] // 2) * EXCHANGE_RATES_TO_EURO[
            DOLLAR], 2)) < 1e-10, 'the half of {} is debited suucessfully'.format(
        (reservation_1['totalAmount'] // 2) * EXCHANGE_RATES_TO_EURO[DOLLAR]))
    assert_and_print(abs(Decimal(balance_after_first_credit - balance_after_confirmation_of_room_1) - (
            Decimal(balance_after_first_credit) - round(
        Decimal(balance_after_first_credit) - reservation_1['totalAmount'] * EXCHANGE_RATES_TO_EURO[DOLLAR],
        2))) < 1e-10, 'the half of {} is debited suucessfully'.format(
        reservation_1['totalAmount'] * EXCHANGE_RATES_TO_EURO[DOLLAR]))
    confirmed_reservation = get_reservation(reservation_1['id'])
    assert_and_print(confirmed_reservation['status'] == 'CONFIRMED',
                     'reservation is in status {}'.format(reservation_1['status']))
    assert_and_print(confirmed_reservation['confirmationDate'] == datetime.now().date().isoformat(),
                     'reservation is done today {}'.format(confirmed_reservation['confirmationDate']))

    assert cancel_reservation(reservation_1['id'])

    deleted_reservation = get_reservation(reservation_1['id'])

    assert_and_print(deleted_reservation['status'] == 'CANCELED',
                     'reservation is in status {}'.format(reservation_1['status']))
    assert_and_print(get_account_balance(created_client['email'], DOLLAR) == balance_after_confirmation_of_room_1,
                     'client balance does not change')


def scenario_2():
    print("""   _____ _____________   _____    ____  ________     ___ 
  / ___// ____/ ____/ | / /   |  / __ \/  _/ __ \   |__ \\
  \__ \/ /   / __/ /  |/ / /| | / /_/ // // / / /   __/ /
 ___/ / /___/ /___/ /|  / ___ |/ _, _// // /_/ /   / __/ 
/____/\____/_____/_/ |_/_/  |_/_/ |_/___/\____/   /____/ 
                                                         
   
                                                        
An user created an account, recharging with euros it's account to take the room of type SUITE,
 as Euro is upper than Dollar it recharge it's account with less than the room price the reservation is done but the
  confirmation failed
 so the user recharge with 2 night to confirm it's reservation
""")
    client_json = {
        "fullName": 'jean',
        "email": 'jean@gmail.com',
        "phoneNumber": '+330634256789'
    }
    # Execute the scenarios
    created_client = create_client(client_json['fullName'], client_json['email'], client_json['phoneNumber'])
    assert_and_print(created_client, 'client created')
    assert_and_print(created_client['fullName'] == client_json['fullName'],
                     'with the client full name {}'.format(client_json['fullName']))
    assert_and_print(created_client['email'] == client_json['email'],
                     'with the client email {}'.format(client_json['email']))
    assert_and_print(created_client['phoneNumber'] == client_json['phoneNumber'],
                     'with the client phone number {}'.format(client_json['phoneNumber']))
    rooms = fetch_rooms()
    assert_and_print(len(rooms) == 3, 'room types is 3')
    # Because Dollar is lower than Euros as Room prices are in Euros

    refund_account(created_client['email'])

    initial_balance = get_account_balance(created_client['email'], EURO)

    assert_and_print(
        initial_balance == 0, 'client balance is {}'.format(initial_balance))

    assert_and_print(
        credit_account(created_client['email'], rooms[2]["pricePerNight"] * (NUMBER_OF_NIGHTS_IN_ROOM_TYPE_TWO - 1),
                       EURO) == True, 'account credited')
    balance_after_first_credit = get_account_balance(created_client['email'], EURO)
    # result.quantize(Decimal('1.'), rounding=ROUND_CEILING)
    # (Decimal(balance_after_first_credit - initial_balance)*EXCHANGE_RATES_TO_EURO[DOLLAR]).quantize(Decimal('1.'), rounding=ROUND_CEILING), ((rooms[0]["pricePerNight"] * (NUMBER_OF_NIGHTS+1))*EXCHANGE_RATES_TO_EURO[DOLLAR]).quantize(Decimal('1.'), rounding=ROUND_CEILING)
    # Decimal(balance_after_first_credit - initial_balance)*EXCHANGE_RATES_TO_EURO[DOLLAR], (rooms[0]["pricePerNight"] * (NUMBER_OF_NIGHTS+1))*EXCHANGE_RATES_TO_EURO[DOLLAR]
    assert_and_print(round(balance_after_first_credit - initial_balance) == (
            rooms[2]["pricePerNight"] * (NUMBER_OF_NIGHTS_IN_ROOM_TYPE_TWO - 1)),
                     f'account credited with the given amount {round(balance_after_first_credit - initial_balance)} in {EURO}')
    reservation_1 = make_reservation(created_client['email'], rooms[2]['type'], NUMBER_OF_NIGHTS_IN_ROOM_TYPE_TWO,
                                     in_x_days=NUMBER_OF_NIGTH_TO_DO_IN_ROOM_1)
    assert_and_print(reservation_1['room'] == rooms[2], f'reservation done with the room {rooms[2]}')
    assert_and_print(reservation_1['client']['email'] == created_client['email'],
                     'reservation done with the mail {}'.format(created_client['email']))
    assert_and_print(get_day_in_x_days(NUMBER_OF_NIGTH_TO_DO_IN_ROOM_1) == reservation_1['checkInDate'],
                     'reservation is done unitl {}'.format(reservation_1['checkInDate']))
    assert_and_print(reservation_1['status'] == 'REGISTERED',
                     'reservation is in status {}'.format(reservation_1['status']))
    assert_and_print(reservation_1['totalAmount'] == rooms[2]['pricePerNight'] * NUMBER_OF_NIGHTS_IN_ROOM_TYPE_TWO,
                     'reservation done with the mail {}'.format(created_client['email']))
    assert_and_print(reservation_1['confirmationDate'] == None, 'reservation not yet confirmed')

    balance_after_reservation_for_room_1 = get_account_balance(created_client['email'], EURO)

    assert_and_print(abs(Decimal(balance_after_reservation_for_room_1) - round(
        Decimal(balance_after_first_credit) - (reservation_1['totalAmount'] // 2) * EXCHANGE_RATES_TO_EURO[EURO],
        2)) < 1e-10, 'the half of {} is debited suucessfully'.format(
        (reservation_1['totalAmount'] // 2) * EXCHANGE_RATES_TO_EURO[EURO]))

    assert_and_print(confirm_reservation(reservation_1['id']).status_code == 406,
                     'reservation cannot be done because of insufficient fund')

    not_confirmed_reservation = get_reservation(reservation_1['id'])

    assert_and_print(not_confirmed_reservation['status'] != 'CONFIRMED',
                     'reservation is not in status {}'.format('CONFIRMED'))
    assert_and_print(not_confirmed_reservation['confirmationDate'] == None,
                     'reservation not confirmed')

    assert_and_print(
        credit_account(created_client['email'], rooms[2]["pricePerNight"],
                       EURO) == True, 'account credited')

    balance_after_first_credit += rooms[2]["pricePerNight"]

    balance_after_reservation_for_room_1 = get_account_balance(created_client['email'], EURO)

    assert_and_print(confirm_reservation(reservation_1['id']).json() == True, 'reservation is confirmed')

    balance_after_confirmation_of_room_1 = get_account_balance(created_client['email'], EURO)

    assert_and_print(abs(Decimal(balance_after_confirmation_of_room_1) - round(
        Decimal(balance_after_reservation_for_room_1) - (reservation_1['totalAmount'] // 2) * EXCHANGE_RATES_TO_EURO[
            EURO], 2)) < 1e-10, 'the half of {} is debited suucessfully'.format(
        (reservation_1['totalAmount'] // 2) * EXCHANGE_RATES_TO_EURO[EURO]))
    assert_and_print(abs(Decimal(balance_after_first_credit - balance_after_confirmation_of_room_1) - (
            Decimal(balance_after_first_credit) - round(
        Decimal(balance_after_first_credit) - reservation_1['totalAmount'] * EXCHANGE_RATES_TO_EURO[EURO],
        2))) < 1e-10, 'the half of {} is debited suucessfully'.format(
        reservation_1['totalAmount'] * EXCHANGE_RATES_TO_EURO[EURO]))
    confirmed_reservation = get_reservation(reservation_1['id'])
    assert_and_print(confirmed_reservation['status'] == 'CONFIRMED',
                     'reservation is in status {}'.format(reservation_1['status']))
    assert_and_print(confirmed_reservation['confirmationDate'] == datetime.now().date().isoformat(),
                     'reservation is done today {}'.format(confirmed_reservation['confirmationDate']))


if __name__ == "__main__":
    scenario_1()
    scenario_2()
