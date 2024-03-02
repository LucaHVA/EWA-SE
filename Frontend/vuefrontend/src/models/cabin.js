class Cabin {
    id;
    image;
    type;
    pricePerWeek;
    location;
    numAvailable;
    descriptionId;

    // List of cabin types
    static type = {
        GEAR: "BeachGear",
        SMALLDT: "SmallDayTime",
        SMALL: "SmallLodge",
        LARGE: "LargeLodge",
        FAMILY: "FamilyLodge"
    }

    static image = {
        IMAGE1: require('@/assets/beachfoto1.jpg'),
        IMAGE2: require('@/assets/beachfoto3.jpg'),
        IMAGE3: require('@/assets/beachfoto5.jpg'),
    };

    // Constructor for the cabins
    constructor(id, image, type, pricePerWeek, location, numAvailable, descriptionId) {
        this.id = id;
        this.image = image;
        this.type = type;
        this.pricePerWeek = pricePerWeek;
        this.location = location;
        this.numAvailable = numAvailable;
        this.descriptionId = descriptionId;
    }

    // Creates a new cabin based on random numbers
    static createSampleCabin(pId = 0) {
        const images = Object.values(Cabin.image);
        const randomImage = images[Math.floor(Math.random() * images.length)];

        const typeArray = Object.values(Cabin.type);
        const indexNumber = Math.floor(Math.random() * typeArray.length);
        const randomType = typeArray[indexNumber];

        const prices = ["100", "200", "300", "400", "500"];
        const randomPrice = prices[indexNumber];

        const locations = ["Noordwijk", "Ommen", "Roggel", "Purmerend"];
        const randomLocation = locations[Math.floor(Math.random() * locations.length)];

        const randomAvailability = Math.floor(Math.random() * 15 + 1);

        const descriptions = ["beautiful ", "modern ", "cute ", "cosy "];
        const randomDescription = descriptions[Math.floor(Math.random() * descriptions.length)];
        const finalDescription = randomDescription + randomType;


        return new Cabin(pId, randomImage, randomType, randomPrice, randomLocation, randomAvailability, finalDescription);
    }

    static createEmptyCabin(pId = 0) {
        return new Cabin(pId, null, null, null, null, null, null);
    }





    static copyConstructor(cabin) {
        if (cabin == null) return null;

        let copy = new Cabin(
            cabin.id,
            cabin.image,
            cabin.type,
            cabin.pricePerWeek,
            cabin.location,
            cabin.numAvailable,
            cabin.descriptionId
        );
        return copy;
    }


    equals(otherCabin) {
        return (
            this.id === otherCabin.id &&
            this.type === otherCabin.type &&
            this.pricePerWeek === otherCabin.pricePerWeek &&
            this.location === otherCabin.location &&
            this.numAvailable === otherCabin.numAvailable &&
            this.descriptionId === otherCabin.descriptionId
        );
    }


}

module.exports = Cabin;