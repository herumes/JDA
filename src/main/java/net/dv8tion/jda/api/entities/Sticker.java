/*
 * Copyright 2015 Austin Keener, Michael Ritter, Florian Spieß, and the JDA contributors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.dv8tion.jda.api.entities;

import net.dv8tion.jda.internal.utils.Helpers;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Set;

/**
 * An object representing a sticker in a Discord message.
 *
 * @see Message#getStickers()
 */
public class Sticker implements ISnowflake
{
    private final long id;
    private final long packId;
    private final String name;
    private final String description;
    private final Set<String> tags;
    private final StickerType type;
    private final StickerFormat formatType;
    private final boolean available;
    private final User user;
    private final int sortValue;

    /** Template for {@link #getIconUrl()} */
    public static final String ICON_URL = "https://cdn.discordapp.com/stickers/%s.%s";

    public Sticker(
            final long id,
            final long packId,
            final String name,
            final String description,
            final Set<String> tags,
            final StickerType type,
            final StickerFormat formatType,
            final boolean available,
            final User user,
            final int sortValue)
    {
        this.id = id;
        this.packId = packId;
        this.name = name;
        this.description = description;
        this.tags = tags;
        this.type = type;
        this.formatType = formatType;
        this.available = available;
        this.user = user;
        this.sortValue = sortValue;
    }

    @Override
    public long getIdLong()
    {
        return id;
    }

    /**
     * The ID of the pack the sticker is from.
     *
     * <p>If this sticker is from a guild, this will be the guild id instead.
     *
     * @return the ID of the pack the sticker is from
     */
    @Nonnull
    public String getPackId()
    {
        return Long.toUnsignedString(getPackIdLong());
    }

    /**
     * The ID of the pack the sticker is from.
     *
     * <p>If this sticker is from a guild, this will be the guild id instead.
     *
     * @return the ID of the pack the sticker is from
     */
    public long getPackIdLong()
    {
        return packId;
    }

    /**
     * The name of the sticker.
     *
     * @return the name of the sticker
     */
    @Nonnull
    public String getName()
    {
        return name;
    }

    /**
     * The description of the sticker or empty String if the sticker doesn't have one.
     *
     * @return Possibly-empty String containing the description of the sticker
     */
    @Nonnull
    public String getDescription()
    {
        return description;
    }

    /**
     * Set of tags of the sticker. Tags can be used instead of the name of the sticker as aliases.
     *
     * @return Possibly-empty unmodifiable Set of tags of the sticker
     */
    @Nonnull
    public Set<String> getTags()
    {
        return tags;
    }

    /**
     * The type of sticker. Will be {@link StickerType#UNKNOWN Unknown} if the sticker is from a message
     *
     * @return The type of sticker
     */
    public StickerType getType()
    {
        return type;
    }

    /**
     * The {@link StickerFormat Format} of the sticker.
     *
     * @return the format of the sticker
     */
    @Nonnull
    public StickerFormat getFormatType()
    {
        return formatType;
    }

    /**
     * If the sticker is available. May be false due to the loss of Server Boosts.
     * Will always be true if the sticker is not a {@link StickerType#GUILD Guild Sticker}.
     *
     * @return A boolean indicating if the sticker is available or not
     */
    public boolean isAvailable()
    {
        return available;
    }

    /**
     * Gets the user that created the sticker. Will be null if the sticker is not a
     * {@link StickerType#GUILD Guild Sticker}
     *
     * @return The user that created the sticker
     */
    @Nullable
    public User getUser()
    {
        return user;
    }

    /**
     * Get's the stickers order in a Sticker Pack. Will be -1 if the sticker is not a
     * {@link StickerType#STANDARD Standard Sticker}
     * @return The sticker's sort value, or -1
     */
    public int getSortValue()
    {
        return sortValue;
    }

    /**
     * The url of the sticker image.
     *
     * @throws java.lang.IllegalStateException
     *         If the {@link StickerFormat StickerFormat} of this sticker is {@link StickerFormat#UNKNOWN UNKNOWN}
     *
     * @return The image url of the sticker
     */
    @Nonnull
    public String getIconUrl()
    {
        return Helpers.format(ICON_URL, getId(), formatType.getExtension());
    }

    public enum StickerFormat
    {
        /**
         * The PNG format.
         */
        PNG(1, "png"),
        /**
         * The APNG format.
         */
        APNG(2, "apng"),
        /**
         * The LOTTIE format.
         * <br>Lottie isn't a standard renderable image. It is a JSON with data that can be rendered using the lottie library.
         *
         * @see <a href="https://airbnb.io/lottie/">Lottie website</a>
         */
        LOTTIE(3, "json"),
        /**
         * Represents any unknown or unsupported {@link Sticker MessageSticker} format types.
         */
        UNKNOWN(-1, null);

        private final int id;
        private final String extension;

        StickerFormat(final int id, final String extension)
        {
            this.id = id;
            this.extension = extension;
        }

        /**
         * The file extension used for the sticker asset.
         *
         * @throws java.lang.IllegalStateException
         *         If the {@link StickerFormat StickerFormat} is {@link StickerFormat#UNKNOWN UNKNOWN}
         *
         * @return The file extension for this format
         */
        @Nonnull
        public String getExtension()
        {
            if (this == UNKNOWN)
                throw new IllegalStateException("Can only get extension of a known format");
            return extension;
        }

        /**
         * Resolves the specified format identifier to the StickerFormat enum constant.
         *
         * @param  id
         *         The id for this format
         *
         * @return The representative StickerFormat or UNKNOWN if it can't be resolved
         */
        @Nonnull
        public static Sticker.StickerFormat fromId(int id)
        {
            for (Sticker.StickerFormat stickerFormat : values())
            {
                if (stickerFormat.id == id)
                    return stickerFormat;
            }
            return UNKNOWN;
        }
    }

    public enum StickerType {
        /**
         * A standard sticker that belongs to a StickerPack
         */
        STANDARD(1),
        /**
         * A Guild sticker that belongs to a Guild
         */
        GUILD(2),
        UNKNOWN(-1);

        private final int id;

        StickerType(final int id)
        {
            this.id = id;
        }

        /**
         * Resolves the specified id to the StickerType enum constant.
         *
         * @param  id
         *         The id for this sticker type
         *
         * @return The representative StickerType or UNKNOWN if it can't be resolved
         */
        @Nonnull
        public static Sticker.StickerType fromId(int id)
        {
            for (Sticker.StickerType stickerType : values())
            {
                if (stickerType.id == id)
                    return stickerType;
            }
            return UNKNOWN;
        }
    }
}
