**Introduction**

The Malaysia Fuel Calculator (BudiMadani) App is a dynamic Android application designed to help motorists in the peninsula calculate their weekly fuel expenditure while checking their eligibility for the BUDI MADANI subsidy program.

The application provides a seamless user experience by automatically distinguishing financial outcomes between standard retail transactions and subsidized fuel consumption.

This tool aims to simplify personal financial management by providing transparent calculations for fuel costs, rebates, and overall savings on a unified dashboard.

**Key Features**

Dynamic Visibility Controls: The user interface intelligently adapts to user input; fields such as 'BUDI Rebate' and 'Total Savings' are displayed exclusively when RON95 is selected and the subsidy option is enabled, ensuring a clutter-free display for RON97 and Diesel users.

Robust Input Validation: Built-in validation structures catch empty inputs or formatting anomalies instantly, preventing runtime application crashes and ensuring data integrity.

Dedicated About Page: Includes a comprehensive author profile, official application branding, a copyright notice, and a direct, clickable repository link to the source code.

**Technical Stack & Architecture**

This app is built natively in Java utilizing Android Studio and leverages ConstraintLayout alongside LinearLayout architectures to deliver a fully responsive UI across mobile and tablet form factors.

Asynchronous network polling is implemented via the Volley HTTP library to parse remote JSON data on a background thread without interrupting the main user interface framework.

Data management utilizes dynamic string formatting and parsing logic (e.g., stripping presentation prefixes via text manipulation) to transition smoothly between user presentation and mathematical calculation.

**How to use the app?**

1) Launch the application and allow it a brief moment to synchronize the latest weekly market prices from the remote directory.

2) Select your desired fuel type (RON95, RON97, or Diesel) from the structured dropdown selection spinner."

3) Input your expected fuel usage in liters within the text field provided."

4) If you select RON95 and qualify for the BUDI MADANI program, toggle the BUDI95 switch to activate your subsidy calculation.

5) Click the 'Calculate' button to render your final breakdown instantly.

To facilitate simple weekly updates without forcing users to update the core Android installation, fuel pricing parameters are hosted externally in a lightweight fuel_prices.json configuration file. That's all I can do for now, as trying to use an API from the data.gov.my website gave us the "Offline" message.

Updating the retail price indices merely requires editing the public JSON schema on the remote repository (which will be updated on Wednesday night, once the Finance Ministry announces the prices for that week); the client applications will parse and apply the updated metrics automatically on their next launch cycle.
